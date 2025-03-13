package project.tripplan.domain.user.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.bookmark.entity.ReviewBookmark;
import project.tripplan.domain.bookmark.repository.BookmarkRepositoryCustom;
import project.tripplan.domain.bookmark.repository.ReviewBookmarkRepositoryCustom;
import project.tripplan.domain.comment.entity.PlanComment;
import project.tripplan.domain.comment.repository.PlanCommentRepositoryCustom;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanRepositoryCustom;
import project.tripplan.domain.point.repository.PointRepositoryCustom;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.review.repository.ReviewRepositoryCustom;
import project.tripplan.domain.user.dto.UserBookmarkRes;
import project.tripplan.domain.user.dto.UserCommentRes;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.dto.UserPointHistoryRes;
import project.tripplan.domain.user.dto.UserProfileReq;
import project.tripplan.domain.user.dto.UserReviewBookmarkRes;
import project.tripplan.domain.user.dto.UserReviewRes;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepository;
import project.tripplan.domain.user.repository.UserRepositoryCustom;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	@Value("${cloud.prefix}")
	private String prefix;

	private final UserRepositoryCustom userRepositoryCustom;
	private final UserRepository userRepository;
	private final PlanRepositoryCustom planRepositoryCustom;
	private final BookmarkRepositoryCustom bookmarkRepositoryCustom;
	private final ReviewBookmarkRepositoryCustom reviewBookmarkRepositoryCustom;
	private final PlanCommentRepositoryCustom planCommentRepositoryCustom;
	private final PlanPlaceCategoryRepositoryCustom planPlaceCategoryRepositoryCustom;
	private final PointRepositoryCustom pointRepositoryCustom;
	private final ReviewRepositoryCustom reviewRepositoryCustom;
	private final S3Service s3Service;

	public void updateUserProfile(Long userId, UserProfileReq req, MultipartFile image) {
		User user = userRepository.findById(userId).get();
		if (req.getNickname() != null) {
			user.setNickname(req.getNickname());
		}
		if (user.getImage() != null) {
			s3Service.deleteFile(user.getImage());
		}
		user.setImage(s3Service.uploadFile(image));
	}

	@Transactional(readOnly = true)
	public Page<UserPlanRes> getUserPlans(Long userId, Pageable pageable) {
		Page<UserPlanRes> findPlans = planRepositoryCustom.findPlansByUserId(userId, pageable);
		List<UserPlanRes> content = findPlans.getContent();

		for (UserPlanRes userPlan : content) {
			userPlan.setThumbnail((userPlan.getThumbnail() == null) ? null : prefix + "/" + userPlan.getThumbnail());
		}
		return new PageImpl<>(content, pageable, findPlans.getTotalElements());
	}

	@Transactional(readOnly = true)
	public Page<UserBookmarkRes> getUserBookmarks(Long userId, Pageable pageable) {

		Page<UserBookmarkRes> findBookmarks = bookmarkRepositoryCustom.findBookmarksByUserId(userId, pageable);
		List<UserBookmarkRes> content = findBookmarks.getContent();

		for (UserBookmarkRes userBookmark : content) {
			userBookmark.setThumbnail(
				(userBookmark.getThumbnail() == null) ? null : prefix + "/" + userBookmark.getThumbnail());
		}
		return new PageImpl<>(content, pageable, findBookmarks.getTotalElements());
	}

	@Transactional(readOnly = true)
	public Page<UserCommentRes> getUserComments(Long userId, Pageable pageable) {
		Page<PlanComment> commentPage = planCommentRepositoryCustom.findCommentsByUser(userId, pageable);

		// 1) 댓글에 연결된 planId 모으기 (중복제거)
		List<Long> planIds = commentPage
			.stream()
			.map(comment -> comment.getPlan().getId())
			.distinct()
			.toList();

		// 2) planIds에 해당하는 PlanPlaceCategory 전부 조회
		List<PlanPlaceCategory> planPlaceCategories = planPlaceCategoryRepositoryCustom.findAllByPlanIds(planIds);

		// 3) planId -> 카테고리명들
		Map<Long, List<String>> planCategoryMap = planPlaceCategories.stream()
			.collect(Collectors.groupingBy(
				ppc -> ppc.getPlan().getId(),
				Collectors.mapping(
					ppc -> ppc.getPlaceCategory().getName(),
					Collectors.toList()
				)
			));

		// 4) Page<Comment> -> Page<UserCommentRes> 변환
		List<UserCommentRes> dtoList = commentPage.getContent().stream()
			.map(c -> {
				Long planId = c.getPlan().getId();
				List<String> categories = planCategoryMap.getOrDefault(planId, List.of());

				return new UserCommentRes(
					planId,
					c.getId(),
					c.getPlan().getTitle(),
					categories,
					c.getCreatedAt(),
					c.getContent()
				);
			})
			.toList();

		// Page<UserCommentRes> 생성
		return new PageImpl<>(dtoList, pageable, commentPage.getTotalElements());
	}

	@Transactional(readOnly = true)
	public Page<UserPointHistoryRes> getUserPointHistory(User user, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return pointRepositoryCustom.findAllWithUser(pageable, user.getId()).map(point -> new UserPointHistoryRes(
			point.getUpdatedAt(),
			point.getPointType(),
			point.getPoint(),
			point.getPointTypeId()
		));
	}

	public Page<UserReviewBookmarkRes> getUserReviewBookmarks(Long userId, Pageable pageable) {
		// 1) ReviewBookmark 목록 조회
		Page<ReviewBookmark> findReviewBookmarks =
			reviewBookmarkRepositoryCustom.findReviewBookmarksByUserId(userId, pageable);

		// 2) Stream API를 이용해 HTML 파싱 + DTO 변환
		List<UserReviewBookmarkRes> mappedList = findReviewBookmarks.stream()
			.map(rb -> {
				// rb(ReviewBookmark) -> review(Review) -> HTML 파싱
				Document doc = Jsoup.parse(rb.getReview().getContent());
				Elements images = doc.select("img[src]");

				// 이미지 개수
				int imageCount = images.size();

				// 첫 번째 이미지 URL
				String contentImageUrl = images.isEmpty() ? null : images.first().attr("src");

				// 본문 텍스트 추출을 위해 img 태그 제거
				images.remove();

				// HTML 태그 제거 후 순수 텍스트만 추출
				String contentText = doc.body().text();

				// 최종적으로 UserReviewBookmarkRes DTO를 빌드하여 반환
				return UserReviewBookmarkRes.builder()
					.reveiwBookmarkId(rb.getId())
					.reviewId(rb.getReview().getId())
					.title(rb.getReview().getTitle())
					.imageCount(imageCount)
					.contentText(contentText)
					.contentImageUrl(contentImageUrl)
					.createdAt(LocalDate.from(rb.getCreatedAt()))
					.build();
			})
			.collect(Collectors.toList());

		// 3) Page<UserReviewBookmarkRes> 형태로 반환
		return new PageImpl<>(mappedList, pageable, findReviewBookmarks.getTotalElements());
	}

	public Page<UserReviewRes> getMyReviews(Long userId, Pageable pageable) {
		// 1) Review 목록 조회
		Page<Review> findReview = reviewRepositoryCustom.findReviewsByUserId(userId, pageable);

		// 2) Stream API를 이용해 HTML 파싱 + DTO 변환
		List<UserReviewRes> mappedList = findReview.stream()
			.map(review -> {
				// review(Review) -> HTML 파싱
				Document doc = Jsoup.parse(review.getContent());
				Elements images = doc.select("img[src]");

				// 이미지 개수
				int imageCount = images.size();

				// 첫 번째 이미지 URL
				String contentImageUrl = images.isEmpty() ? null : images.first().attr("src");

				// 본문 텍스트 추출을 위해 img 태그 제거
				images.remove();

				// HTML 태그 제거 후 순수 텍스트만 추출
				String contentText = doc.body().text();

				// 최종적으로 UserReviewRes DTO를 빌드하여 반환
				return UserReviewRes.builder()
					.reviewId(review.getId())
					.title(review.getTitle())
					.imageCount(imageCount)
					.contentText(contentText)
					.contentImageUrl(contentImageUrl)
					.createdAt(LocalDate.from(review.getCreatedAt()))
					.build();
			})
			.collect(Collectors.toList());

		// 3) Page<UserReviewRes> 형태로 반환
		return new PageImpl<>(mappedList, pageable, findReview.getTotalElements());
	}
}


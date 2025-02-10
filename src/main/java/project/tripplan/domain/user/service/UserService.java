package project.tripplan.domain.user.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.bookmark.repository.BookmarkRepositoryCustom;
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.comment.repository.CommentRepositoryCustom;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanRepositoryCustom;
import project.tripplan.domain.user.dto.UserBookmarkRes;
import project.tripplan.domain.user.dto.UserCommentRes;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.dto.UserPlansDraftsRes;
import project.tripplan.domain.user.dto.UserProfileReq;
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
	private final CommentRepositoryCustom commentRepositoryCustom;
	private final PlanPlaceCategoryRepositoryCustom planPlaceCategoryRepositoryCustom;
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
		Page<Comment> commentPage = commentRepositoryCustom.findCommentsByUser(userId, pageable);

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
	public Page<UserPlansDraftsRes> getUserPlanDrafts(User user, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return planPlaceCategoryRepositoryCustom.findAllByUserIdWithPlan(
			user.getId(), pageable);
	}
}


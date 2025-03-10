package project.tripplan.domain.review.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.point.entity.Point;
import project.tripplan.domain.point.enums.PointStatus;
import project.tripplan.domain.point.enums.PointType;
import project.tripplan.domain.point.repository.PointRepository;
import project.tripplan.domain.review.dto.AddReviewReq;
import project.tripplan.domain.review.dto.PlaceReviewRes;
import project.tripplan.domain.review.dto.ReviewDto;
import project.tripplan.domain.review.dto.ReviewRes;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.review.repository.ReviewRepository;
import project.tripplan.domain.review.repository.ReviewRepositoryCustom;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.enums.UserRole;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

	@Value("${cloud.prefix}")
	private String prefix;

	private final ReviewRepositoryCustom reviewRepositoryCustom;
	private final ReviewRepository reviewRepository;
	private final S3Service s3Service;
	private final PointRepository pointRepository;

	public Long addReview(User user, AddReviewReq reviewReq) {
		Review review = Review.builder()
			.user(user)
			.placeId(reviewReq.getPlaceId())
			.title(reviewReq.getTitle())
			.content(reviewReq.getContent())
			.latitude(reviewReq.getLatitude())
			.longitude(reviewReq.getLongitude())
			.visitedDay(reviewReq.getVisitedDay())
			.averageRating(reviewReq.getAverageRating())
			.build();

		reviewRepository.save(review);

		Point point = Point.builder()
			.user(user)
			.pointType(PointType.REVIEW)
			.pointTypeId(review.getId())
			.pointStatus(PointStatus.PENDING)
			.point(100)
			.build();

		pointRepository.save(point);

		return review.getId();
	}

	//조회수 증가가 있기 떄문에 readOnly = false
	@Transactional
	public ReviewRes getReview(Long reviewId) {

		Review review = reviewRepositoryCustom.findReviewIdWithUser(reviewId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.REVIEW_NOT_EXIST));

		//조회수 증가
		review.increaseViewCount();

		return ReviewRes.builder()
			.id(review.getId())
			.placeId(review.getPlaceId())
			.title(review.getTitle())
			.nickname(review.getUser().getNickname())
			.userImage(review.getUser().getImage())
			.content(review.getContent())
			.viewCount(review.getViewCount())
			.latitude(review.getLatitude())
			.longitude(review.getLongitude())
			.visitedDay(review.getVisitedDay())
			.averageRating(review.getAverageRating())
			.build();
	}

	public void deleteReview(User user, Long reviewId) {
		Review review = reviewRepositoryCustom.findReviewIdWithUser(reviewId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.REVIEW_NOT_EXIST));

		if (review.getUser().getId() != user.getId() && user.getUserRole() != UserRole.ADMIN) {
			// 관리자가 아니면서 본인 댓글이 아닌 댓글을 삭제하려는 경우
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_DELETE_REVIEW);
		}

		reviewRepository.delete(review);
	}

	public PlaceReviewRes getPlaceIdOtherReview(User user, String placeId) {
		List<Review> reviews = reviewRepositoryCustom.findByPlaceIdAndUserNot(placeId, user);

		List<ReviewDto> summaries = reviews.stream()
			.map(review -> {
				// HTML 파싱하여 텍스트와 (첫) 이미지 URL 추출
				String contentHtml = review.getContent();

				// 1) Jsoup으로 문서 파싱
				Document doc = Jsoup.parse(contentHtml);

				// 2) img 태그 추출
				Elements images = doc.select("img[src]");

				// 이미지 개수
				int imageCount = images.size();

				// 3) 첫 번째 이미지 URL만 추출
				String contentImageUrl = images.isEmpty() ? null : images.first().attr("src");

				// 4) 나머지 텍스트만 추출하기 위해 이미지 태그 제거
				images.remove();

				// 5) HTML 태그를 모두 제거하고 일반 텍스트만 꺼냄
				String contentText = doc.body().text();

				// DTO 빌드
				return ReviewDto.builder()
					.reviewId(review.getId())
					.title(review.getTitle())
					.createdAt(LocalDate.from(review.getCreatedAt()))
					.userImageUrl(
						review.getUser().getImage() != null
							? prefix + "/" + review.getUser().getImage()
							: null
					)
					.nickname(review.getUser().getNickname())
					.contentText(contentText)
					.contentImageUrl(contentImageUrl)
					.imageCount(imageCount)
					.build();
			})
			.collect(Collectors.toList());

		return PlaceReviewRes.builder()
			.totalReviewCount((long)reviews.size())
			.reviewSummaries(summaries)
			.build();
	}
}

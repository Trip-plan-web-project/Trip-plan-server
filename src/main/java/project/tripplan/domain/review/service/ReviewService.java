package project.tripplan.domain.review.service;

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
}

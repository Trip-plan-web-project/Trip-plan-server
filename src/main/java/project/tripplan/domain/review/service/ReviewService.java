package project.tripplan.domain.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.review.dto.AddReviewReq;
import project.tripplan.domain.review.dto.ReviewRes;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.review.repository.ReviewRepository;
import project.tripplan.domain.review.repository.ReviewRepositoryCustom;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ReviewService {

	private final ReviewRepositoryCustom reviewRepositoryCustom;
	private final ReviewRepository reviewRepository;
	private final S3Service s3Service;

	public Long addReview(User user, AddReviewReq reviewReq) {
		Review review = Review.builder()
			.user(user)
			.title(reviewReq.getTitle())
			.content(reviewReq.getContent())
			.latitude(reviewReq.getLatitude())
			.longitude(reviewReq.getLongitude())
			.visitedDay(reviewReq.getVisitedDay())
			.build();

		reviewRepository.save(review);

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
}

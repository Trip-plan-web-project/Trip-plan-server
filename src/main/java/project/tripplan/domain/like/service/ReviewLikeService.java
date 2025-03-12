package project.tripplan.domain.like.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.like.entity.ReviewLike;
import project.tripplan.domain.like.repository.ReviewLikeRepository;
import project.tripplan.domain.like.repository.ReviewLikeRepositoryCustom;
import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.review.repository.ReviewRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewLikeService {

	private final ReviewLikeRepository reviewLikeRepository;
	private final ReviewLikeRepositoryCustom reviewLikeRepositoryCustom;
	private final ReviewRepository reviewRepository;

	public Long addReviewLike(User user, Long reviewId) {
		Review findReview = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		reviewLikeRepositoryCustom.findByReviewIdAndUserId(reviewId, user.getId())
			.ifPresent(existingBookmark -> {
				throw new CustomException(BaseResponseCode.REVIEW_LIKE_ALREADY_EXISTS);
			});

		ReviewLike reviewLike = ReviewLike.builder()
			.user(user)
			.review(findReview)
			.build();

		reviewLikeRepository.save(reviewLike);

		return reviewLike.getId();
	}

	public void deleteReviewLike(User user, Long reviewLikeId) {
		ReviewLike findReviewLike = reviewLikeRepositoryCustom.findReviewLikeWithUser(reviewLikeId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.REVIEWLIKE_NOT_EXIST));

		if (findReviewLike.getUser().getId() != user.getId()) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_REVIVELIKE_DELETE);
		}

		reviewLikeRepository.delete(findReviewLike);
	}
}

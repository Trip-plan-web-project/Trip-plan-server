package project.tripplan.domain.like.repository;

import java.util.Optional;

import project.tripplan.domain.like.entity.ReviewLike;

public interface ReviewLikeRepositoryCustom {

	Optional<ReviewLike> findReviewLikeWithUser(Long reviewLikeId);

	Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long userId);

	Long countLikesByReviewId(Long reviewId);
}

package project.tripplan.domain.review.repository;

import java.util.Optional;

import project.tripplan.domain.review.entity.Review;

public interface ReviewRepositoryCustom {

	Optional<Review> findReviewIdWithUser(Long reviewId);
}

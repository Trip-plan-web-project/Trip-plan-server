package project.tripplan.domain.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.review.entity.Review;

public interface ReviewRepositoryCustom {

	Optional<Review> findReviewIdWithUser(Long reviewId);

	List<Review> findByReviewIdNot(Long reviewId, Long userId);

	List<Review> findByAllReview();

	Page<Review> findReviewsByUserId(Long userId, Pageable pageable);

}

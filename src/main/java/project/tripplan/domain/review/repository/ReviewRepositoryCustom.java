package project.tripplan.domain.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.user.entity.User;

public interface ReviewRepositoryCustom {

	Optional<Review> findReviewIdWithUser(Long reviewId);

	List<Review> findByPlaceIdAndUserNot(String placeId, User user);

	List<Review> findByAllReview();

	Page<Review> findReviewsByUserId(Long userId, Pageable pageable);

}

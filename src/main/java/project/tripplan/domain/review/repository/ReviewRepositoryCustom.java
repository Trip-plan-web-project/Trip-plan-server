package project.tripplan.domain.review.repository;

import java.util.List;
import java.util.Optional;

import project.tripplan.domain.review.entity.Review;
import project.tripplan.domain.user.entity.User;

public interface ReviewRepositoryCustom {

	Optional<Review> findReviewIdWithUser(Long reviewId);

	public List<Review> findByPlaceIdAndUserNot(String placeId, User user);
}

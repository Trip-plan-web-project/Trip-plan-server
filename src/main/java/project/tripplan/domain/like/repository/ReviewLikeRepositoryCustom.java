package project.tripplan.domain.like.repository;

import java.util.Optional;

import project.tripplan.domain.like.entity.ReviewLike;

public interface ReviewLikeRepositoryCustom {

	Optional<ReviewLike> findReviewLikeWithUser(Long reviewLikeId);
}

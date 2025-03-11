package project.tripplan.domain.like.repository;

import java.util.Optional;

import project.tripplan.domain.like.entity.PlanLike;

public interface PlanLikeRepositoryCustom {
	Long countLikesByPlanId(Long planId);

	Optional<PlanLike> findPlanLikeWithUser(Long planLikeId);

	Optional<PlanLike> findPlanLikeWithUserAndPlan(Long userId, Long planId);
}

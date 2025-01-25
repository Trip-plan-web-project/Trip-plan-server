package project.tripplan.domain.planLike.repository;

import java.util.Optional;

import project.tripplan.domain.planLike.entity.PlanLike;

public interface PlanLikeRepositoryCustom {
	Long countLikesByPlanId(Long planId);

	Optional<PlanLike> findPlanLikeWithUser(Long planLikeId);
}

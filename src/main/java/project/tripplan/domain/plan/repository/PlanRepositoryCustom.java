package project.tripplan.domain.plan.repository;

import java.util.Optional;

import project.tripplan.domain.plan.entity.Plan;

public interface PlanRepositoryCustom {
	Optional<Plan> findByPlanIdWithUser(Long planId);
}

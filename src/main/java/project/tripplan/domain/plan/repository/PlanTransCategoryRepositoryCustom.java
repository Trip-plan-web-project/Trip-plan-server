package project.tripplan.domain.plan.repository;

import java.util.Optional;

import project.tripplan.domain.plan.entity.PlanTransportationCategory;

public interface PlanTransCategoryRepositoryCustom {
	Optional<PlanTransportationCategory> findByPlanIdWithPlanTransCategory(Long planId);
}

package project.tripplan.domain.plan.repository;

import java.util.List;

import project.tripplan.domain.plan.entity.PlanPlaceCategory;

public interface PlanPlaceCategoryRepositoryCustom {
	List<PlanPlaceCategory> findAllByPlanIdWithPlanAndPlace(Long planId);
}

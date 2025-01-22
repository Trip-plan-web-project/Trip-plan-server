package project.tripplan.domain.plan.repository;

import java.util.List;
import java.util.Optional;

import project.tripplan.domain.plan.dto.PlanNoOffsetReq;
import project.tripplan.domain.plan.entity.Plan;

public interface PlanRepositoryCustom {
	Optional<Plan> findByPlanIdWithUser(Long planId);

	List<Plan> searchPlanNoOffset(PlanNoOffsetReq req);

	List<Plan> findMostViewedPlans(int limit);

	List<Plan> findMostRecentPlans(int limit);

	List<Plan> findHotPlacePlans(String placeName, int limit);
}

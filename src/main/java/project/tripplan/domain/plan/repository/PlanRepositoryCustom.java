package project.tripplan.domain.plan.repository;

import java.util.List;
import java.util.Optional;

import project.tripplan.domain.plan.dto.PlanNoOffsetReq;
import project.tripplan.domain.plan.entity.Plan;
<<<<<<< Updated upstream
=======
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
>>>>>>> Stashed changes

public interface PlanRepositoryCustom {
	Optional<Plan> findByPlanIdWithUser(Long planId);

<<<<<<< Updated upstream
	public List<Plan> searchPlanNoOffset(PlanNoOffsetReq req);
=======
	List<Plan> searchPlanNoOffset(PlanNoOffsetReq req);

	List<Plan> findMostViewedPlans(int limit);

	List<Plan> findMostRecentPlans(int limit);

	Page<UserPlanRes> findPlansByUserId(Long userId, Pageable pageable);
>>>>>>> Stashed changes
}

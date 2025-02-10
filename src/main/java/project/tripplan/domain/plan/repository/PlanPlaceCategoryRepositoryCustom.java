package project.tripplan.domain.plan.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.user.dto.UserPlansDraftsRes;

public interface PlanPlaceCategoryRepositoryCustom {
	List<PlanPlaceCategory> findAllByPlanIdWithPlanAndPlace(Long planId);

	List<PlanPlaceCategory> findAllByPlanIds(List<Long> planIds);

	List<PlanPlaceCategory> findHotPlacesByPlaceName(String placeName, int limit);

	Page<UserPlansDraftsRes> findAllByUserIdWithPlan(Long userId, Pageable pageable);
}

package project.tripplan.domain.planDayDetail.repository;

import java.util.List;

import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;

public interface PlanDayDetailRepositoryCustom {
	List<PlanDayDetail> findAllByPlanIdWithPlanCategory(Long planId);
}

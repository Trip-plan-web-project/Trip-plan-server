package project.tripplan.domain.plan.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.plan.dto.PlanSearchReq;
import project.tripplan.domain.plan.entity.Plan;

public interface PlanRepositoryCustom {
	Optional<Plan> findByPlanIdWithUser(Long planId);

	Page<Plan> searchPlan(PlanSearchReq req, Pageable pageable);
}

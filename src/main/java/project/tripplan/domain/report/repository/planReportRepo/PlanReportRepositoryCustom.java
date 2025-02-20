package project.tripplan.domain.report.repository.planReportRepo;

import java.util.Optional;

import project.tripplan.domain.report.entity.PlanReport;

public interface PlanReportRepositoryCustom {
	Optional<PlanReport> findByUserIdAndPlanId(Long userId, Long planId);
}

package project.tripplan.domain.report.repository.planReportReasonRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.admin.dto.ReportedPlanListRes;

public interface PlanReportReasonRepositoryCustom {
	Page<ReportedPlanListRes> searchReportedPlanList(Pageable pageable, Long reasonId, String startDate,
		String endDate);
}

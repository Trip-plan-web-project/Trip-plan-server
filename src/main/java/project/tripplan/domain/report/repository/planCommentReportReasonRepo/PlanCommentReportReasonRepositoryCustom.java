package project.tripplan.domain.report.repository.planCommentReportReasonRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.admin.dto.ReportedPlanCommentsRes;

public interface PlanCommentReportReasonRepositoryCustom {
	Page<ReportedPlanCommentsRes> findReportedPlanComments(Pageable pageable);
}

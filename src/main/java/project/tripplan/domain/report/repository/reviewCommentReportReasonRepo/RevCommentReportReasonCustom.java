package project.tripplan.domain.report.repository.reviewCommentReportReasonRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.admin.dto.ReportedReviewCommentsRes;

public interface RevCommentReportReasonCustom {
	Page<ReportedReviewCommentsRes> searchReportedReviewComments(Pageable pageable, Long reasonId, String startDate,
		String endDate);
}

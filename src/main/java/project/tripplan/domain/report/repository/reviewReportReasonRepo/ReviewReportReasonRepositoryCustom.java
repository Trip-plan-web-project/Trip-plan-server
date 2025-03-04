package project.tripplan.domain.report.repository.reviewReportReasonRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.admin.dto.ReportedReviewRes;

public interface ReviewReportReasonRepositoryCustom {
	Page<ReportedReviewRes> findReportedReviews(Pageable pageable);
}

package project.tripplan.domain.report.repository.reviewCommentReportRepo;

import java.util.Optional;

import project.tripplan.domain.report.entity.ReviewCommentReport;

public interface ReviewCommentReportRepositoryCustom {
	Optional<ReviewCommentReport> findByUserIdAndReportId(Long userId, Long reportId);
}

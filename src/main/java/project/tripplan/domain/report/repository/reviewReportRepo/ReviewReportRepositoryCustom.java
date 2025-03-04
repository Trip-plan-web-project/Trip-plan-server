package project.tripplan.domain.report.repository.reviewReportRepo;

import java.util.Optional;

import project.tripplan.domain.report.entity.ReviewReport;

public interface ReviewReportRepositoryCustom {
	Optional<ReviewReport> findByUserIdAndReviewId(Long userId, Long reviewId);
}

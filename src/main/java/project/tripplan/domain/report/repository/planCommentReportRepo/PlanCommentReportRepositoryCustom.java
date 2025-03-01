package project.tripplan.domain.report.repository.planCommentReportRepo;

import java.util.Optional;

import project.tripplan.domain.report.entity.PlanCommentReport;

public interface PlanCommentReportRepositoryCustom {
	Optional<PlanCommentReport> findByUserIdAndCommentId(Long userId, Long commentId);
}

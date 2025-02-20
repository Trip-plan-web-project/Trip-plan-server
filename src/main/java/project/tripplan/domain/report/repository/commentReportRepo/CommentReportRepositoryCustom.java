package project.tripplan.domain.report.repository.commentReportRepo;

import java.util.Optional;

import project.tripplan.domain.report.entity.CommentReport;

public interface CommentReportRepositoryCustom {
	Optional<CommentReport> findByUserIdAndCommentId(Long userId, Long commentId);
}

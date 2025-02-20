package project.tripplan.domain.report.repository.commentReportRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.report.entity.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
}

package project.tripplan.domain.report.repository.reviewCommentReportRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.report.entity.ReviewCommentReport;

public interface ReviewCommentReportRepository extends JpaRepository<ReviewCommentReport, Long> {
}

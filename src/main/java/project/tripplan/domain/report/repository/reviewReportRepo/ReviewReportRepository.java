package project.tripplan.domain.report.repository.reviewReportRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.report.entity.ReviewReport;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
}

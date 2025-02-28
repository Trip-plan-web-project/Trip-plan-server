package project.tripplan.domain.report.repository.planCommentReportRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.report.entity.PlanCommentReport;

public interface PlanCommentReportRepository extends JpaRepository<PlanCommentReport, Long> {
}

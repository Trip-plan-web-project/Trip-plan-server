package project.tripplan.domain.report.repository.planReportRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.report.entity.PlanReport;

public interface PlanReportRepository extends JpaRepository<PlanReport, Long> {
}

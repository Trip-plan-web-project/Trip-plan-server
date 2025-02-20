package project.tripplan.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.report.entity.PlanReportReason;

public interface PlanReportReasonRepository extends JpaRepository<PlanReportReason, Long> {
}

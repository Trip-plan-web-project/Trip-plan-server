package project.tripplan.domain.report.repository.planReportRepo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.report.entity.PlanReport;
import project.tripplan.domain.report.entity.QPlanReport;
import project.tripplan.domain.report.entity.QPlanReportReason;
import project.tripplan.domain.report.entity.QReportReason;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanReportRepositoryCustomImpl implements PlanReportRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlanReport planReport = QPlanReport.planReport;
	private final QPlan plan = QPlan.plan;
	private final QUser user = QUser.user;
	private final QPlanReportReason planReportReason = QPlanReportReason.planReportReason;
	private final QReportReason reportReason = QReportReason.reportReason1;

	@Override
	public Optional<PlanReport> findByUserIdAndPlanId(Long userId, Long planId) {
		return Optional.ofNullable(
			qf.selectFrom(planReport)
				.join(planReport.user, user).fetchJoin()
				.join(planReport.plan, plan).fetchJoin()
				.where(user.id.eq(userId).and(plan.id.eq(planId)))
				.fetchOne()
		);
	}
}

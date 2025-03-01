package project.tripplan.domain.report.repository.planReportReasonRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.admin.dto.ReportedPlanListRes;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.report.entity.QPlanReport;
import project.tripplan.domain.report.entity.QPlanReportReason;
import project.tripplan.domain.report.entity.QReportReason;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanReportReasonRepositoryCustomImpl implements PlanReportReasonRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QUser reporter = new QUser("reporter");
	private final QUser reported = new QUser("reported");
	private final QPlan plan = QPlan.plan;
	private final QPlanReport planReport = QPlanReport.planReport;
	private final QPlanReportReason planReportReason = QPlanReportReason.planReportReason;
	private final QReportReason reportReason = QReportReason.reportReason1;

	@Override
	public Page<ReportedPlanListRes> findReportedPlanListByDto(Pageable pageable) {
		List<ReportedPlanListRes> results = qf
			.select(Projections.constructor(ReportedPlanListRes.class,
				planReport.plan.id,
				planReport.id,
				reporter.nickname,
				reported.nickname,
				planReport.plan.title,
				Expressions.stringTemplate("'일정'"),
				planReport.createdAt,
				ExpressionUtils.as(
					Expressions.stringTemplate("GROUP_CONCAT({0})", reportReason.id), "reasonIds"
				)
			))
			.from(planReportReason)
			.join(planReportReason.planReport, planReport)
			.join(planReport.plan, plan)
			.join(planReport.user, reporter)
			.join(plan.user, reported)
			.join(planReportReason.reportReason, reportReason)
			.groupBy(planReport.id)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 전체 개수 조회 (PlanReport 개수 기준)
		Long total = Optional.ofNullable(
			qf.select(planReport.count())
				.from(planReportReason)
				.join(planReportReason.planReport, planReport)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(results, pageable, total);
	}

}

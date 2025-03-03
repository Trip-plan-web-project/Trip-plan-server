package project.tripplan.domain.report.repository.planCommentReportReasonRepo;

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
import project.tripplan.domain.admin.dto.ReportedPlanCommentsRes;
import project.tripplan.domain.comment.entity.QPlanComment;
import project.tripplan.domain.report.entity.QPlanCommentReport;
import project.tripplan.domain.report.entity.QPlanCommentReportReason;
import project.tripplan.domain.report.entity.QReportReason;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanCommentReportReasonRepositoryCustomImpl implements PlanCommentReportReasonRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QUser reporter = new QUser("reporter");
	private final QUser reported = new QUser("reported");
	private final QPlanComment planComment = QPlanComment.planComment;
	private final QPlanCommentReport planCommentReport = QPlanCommentReport.planCommentReport;
	private final QPlanCommentReportReason planCommentReportReason = QPlanCommentReportReason.planCommentReportReason;
	private final QReportReason reportReason = QReportReason.reportReason1;


	@Override
	public Page<ReportedPlanCommentsRes> findReportedPlanComments(Pageable pageable) {
		List<ReportedPlanCommentsRes> results = qf.select(Projections.constructor(ReportedPlanCommentsRes.class,
				planCommentReport.planComment.id,
				planCommentReport.id,
				reporter.nickname,
				reported.nickname,
				planCommentReport.planComment.content,
				Expressions.stringTemplate("'일정 댓글'"),
				planCommentReportReason.planCommentReport.createdAt,
				ExpressionUtils.as(
					Expressions.stringTemplate("GROUP_CONCAT({0})", reportReason.id), "reasonIds"
				)
			))
			.from(planCommentReportReason)
			.join(planCommentReportReason.planCommentReport, planCommentReport)
			.join(planCommentReportReason.reportReason, reportReason)
			.join(planCommentReport.planComment, planComment)
			.join(planCommentReport.user, reporter)
			.join(planComment.user, reported)
			.groupBy(planCommentReport.id)
			.orderBy(planCommentReport.createdAt.desc(), planCommentReport.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = Optional.ofNullable(
			qf.select(planCommentReport.count())
				.from(planCommentReportReason)
				.join(planCommentReportReason.planCommentReport, planCommentReport)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(results, pageable, total);
	}
}

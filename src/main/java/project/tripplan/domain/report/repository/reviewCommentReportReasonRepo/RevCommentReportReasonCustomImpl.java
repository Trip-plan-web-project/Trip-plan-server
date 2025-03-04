package project.tripplan.domain.report.repository.reviewCommentReportReasonRepo;

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
import project.tripplan.domain.admin.dto.ReportedReviewCommentsRes;
import project.tripplan.domain.comment.entity.QReviewComment;
import project.tripplan.domain.report.entity.QReportReason;
import project.tripplan.domain.report.entity.QReviewCommentReport;
import project.tripplan.domain.report.entity.QReviewCommentReportReason;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class RevCommentReportReasonCustomImpl implements RevCommentReportReasonCustom{

	private final JPAQueryFactory qf;
	private final QUser reporter = new QUser("reporter");
	private final QUser reported = new QUser("reported");
	private final QReportReason reportReason = QReportReason.reportReason1;
	private final QReviewCommentReport reviewCommentReport = QReviewCommentReport.reviewCommentReport;
	private final QReviewCommentReportReason reviewCommentReportReason = QReviewCommentReportReason.reviewCommentReportReason;
	private final QReviewComment reviewComment = QReviewComment.reviewComment;

	@Override
	public Page<ReportedReviewCommentsRes> findReportedReviewComments(Pageable pageable) {
		List<ReportedReviewCommentsRes> results = qf.select(Projections.constructor(ReportedReviewCommentsRes.class,
				reviewCommentReport.reviewComment.id,
				reviewCommentReport.id,
				reporter.nickname,
				reported.nickname,
				reviewCommentReport.reviewComment.content,
				Expressions.stringTemplate("'후기 댓글'"),
				reviewCommentReport.createdAt,
				ExpressionUtils.as(
					Expressions.stringTemplate("GROUP_CONCAT({0})", reportReason.id), "reasonIds"
				)
			))
			.from(reviewCommentReportReason)
			.join(reviewCommentReportReason.reviewCommentReport, reviewCommentReport)
			.join(reviewCommentReportReason.reportReason, reportReason)
			.join(reviewCommentReport.reviewComment, reviewComment)
			.join(reviewCommentReport.user, reporter)
			.join(reviewComment.user, reported)
			.groupBy(reviewCommentReport.id)
			.orderBy(reviewCommentReport.createdAt.desc(), reviewCommentReport.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = Optional.ofNullable(
			qf.select(reviewCommentReport.count())
				.from(reviewCommentReportReason)
				.join(reviewCommentReportReason.reviewCommentReport, reviewCommentReport)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(results, pageable, total);
	}
}

package project.tripplan.domain.report.repository.reviewCommentReportReasonRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.admin.dto.ReportedReviewCommentsRes;
import project.tripplan.domain.comment.entity.QReviewComment;
import project.tripplan.domain.report.entity.QReportReason;
import project.tripplan.domain.report.entity.QReviewCommentReport;
import project.tripplan.domain.report.entity.QReviewCommentReportReason;
import project.tripplan.domain.review.entity.QReview;
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
	private final QReview review = QReview.review;

	@Override
	public Page<ReportedReviewCommentsRes> searchReportedReviewComments(Pageable pageable, Long reasonId,
		String startDate, String endDate) {

		BooleanBuilder conditions = createSearchConditions(reasonId, startDate, endDate);

		List<ReportedReviewCommentsRes> results = qf.select(Projections.constructor(ReportedReviewCommentsRes.class,
				reviewCommentReport.reviewComment.id,
				reviewComment.review.id,
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
			.join(reviewComment.review, review)
			.where(conditions)
			.groupBy(reviewCommentReport.id)
			.orderBy(reviewCommentReport.createdAt.desc(), reviewCommentReport.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = Optional.ofNullable(
			qf.select(reviewCommentReport.count())
				.from(reviewCommentReport)
				.where(conditions)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(results, pageable, total);
	}

	private BooleanBuilder createSearchConditions(Long reasonId, String startDate, String endDate) {
		BooleanBuilder conditions = new BooleanBuilder();

		if (reasonId != null) {
			conditions.and(reviewCommentReport.id.in(
				JPAExpressions.select(reviewCommentReportReason.reviewCommentReport.id)
					.from(reviewCommentReportReason)
					.join(reviewCommentReportReason.reportReason, reportReason)
					.where(reviewCommentReportReason.reportReason.id.eq(reasonId))
			));
		}

		if (startDate != null && endDate != null) {
			LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
			LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
			conditions.and(reviewCommentReport.createdAt.between(start, end));
		}

		return conditions;
	}


}

package project.tripplan.domain.report.repository.reviewReportReasonRepo;

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
import project.tripplan.domain.admin.dto.ReportedReviewRes;
import project.tripplan.domain.report.entity.QReportReason;
import project.tripplan.domain.report.entity.QReviewReport;
import project.tripplan.domain.report.entity.QReviewReportReason;
import project.tripplan.domain.review.entity.QReview;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class ReviewReportReasonRepositoryCustomImpl implements ReviewReportReasonRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QUser reporter = new QUser("reporter");
	private final QUser reported = new QUser("reported");
	private final QReviewReport reviewReport = QReviewReport.reviewReport;
	private final QReviewReportReason reviewReportReason = QReviewReportReason.reviewReportReason;
	private final QReportReason reportReason = QReportReason.reportReason1;
	private final QReview review = QReview.review;

	@Override
	public Page<ReportedReviewRes> findReportedReviews(Pageable pageable) {
		List<ReportedReviewRes> results = qf.select(Projections.constructor(ReportedReviewRes.class,
				reviewReport.review.id,
				reviewReport.id,
				reporter.nickname,
				reported.nickname,
				reviewReport.review.title,
				Expressions.stringTemplate("'후기'"),
				reviewReport.createdAt,
				ExpressionUtils.as(
					Expressions.stringTemplate("GROUP_CONCAT({0})", reportReason.id), "reasonIds"
				)
			))
			.from(reviewReportReason)
			.join(reviewReportReason.reportReason, reportReason)
			.join(reviewReportReason.reviewReport, reviewReport)
			.join(reviewReport.user, reporter)
			.join(reviewReport.review, review)
			.join(review.user, reported)
			.groupBy(reviewReport.id)
			.orderBy(reviewReport.createdAt.desc(), reviewReport.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = Optional.ofNullable(
			qf.select(reviewReport.count())
				.from(reviewReportReason)
				.join(reviewReportReason.reviewReport, reviewReport)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(results, pageable, total);
	}

	@Override
	public Page<ReportedReviewRes> searchReportedReviews(Pageable pageable, Long reasonId, String startDate,
		String endDate) {
		BooleanBuilder conditions = createSearchConditions(reasonId, startDate, endDate);

		List<ReportedReviewRes> results = qf.select(Projections.constructor(ReportedReviewRes.class,
				reviewReport.review.id,
				reviewReport.id,
				reporter.nickname,
				reported.nickname,
				reviewReport.review.title,
				Expressions.stringTemplate("'후기'"),
				reviewReport.createdAt,
				ExpressionUtils.as(
					Expressions.stringTemplate("GROUP_CONCAT({0})", reportReason.id), "reasonIds"
				)
			))
			.from(reviewReportReason)
			.join(reviewReportReason.reportReason, reportReason)
			.join(reviewReportReason.reviewReport, reviewReport)
			.join(reviewReport.user, reporter)
			.join(reviewReport.review, review)
			.join(review.user, reported)
			.where(conditions)
			.groupBy(reviewReport.id)
			.orderBy(reviewReport.createdAt.desc(), reviewReport.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = Optional.ofNullable(
			qf.select(reviewReport.count())
				.from(reviewReportReason)
				.join(reviewReportReason.reviewReport, reviewReport)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(results, pageable, total);
	}

	private BooleanBuilder createSearchConditions(Long reasonId, String startDate, String endDate) {
		BooleanBuilder conditions = new BooleanBuilder();

		if (reasonId != null) {
			conditions.and(reviewReport.id.in(
				JPAExpressions.select(reviewReportReason.reviewReport.id)
					.from(reviewReportReason)
					.join(reviewReportReason.reportReason, reportReason)
					.where(reviewReportReason.reportReason.id.eq(reasonId))
			));
		}

		if (startDate != null && endDate != null) {
			LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
			LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
			conditions.and(reviewReport.createdAt.between(start, end));
		}

		return conditions;
	}
}

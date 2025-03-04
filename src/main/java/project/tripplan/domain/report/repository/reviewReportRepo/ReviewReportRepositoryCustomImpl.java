package project.tripplan.domain.report.repository.reviewReportRepo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.report.entity.QReviewReport;
import project.tripplan.domain.report.entity.ReviewReport;
import project.tripplan.domain.review.entity.QReview;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class ReviewReportRepositoryCustomImpl implements ReviewReportRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QReviewReport reviewReport = QReviewReport.reviewReport;
	private final QUser user = QUser.user;
	private final QReview review = QReview.review;

	@Override
	public Optional<ReviewReport> findByUserIdAndReviewId(Long userId, Long reviewId) {
		return Optional.ofNullable(
			qf.selectFrom(reviewReport)
				.join(reviewReport.user, user).fetchJoin()
				.join(reviewReport.review, review).fetchJoin()
				.where(user.id.eq(userId).and(review.id.eq(reviewId)))
				.fetchOne()
		);
	}
}

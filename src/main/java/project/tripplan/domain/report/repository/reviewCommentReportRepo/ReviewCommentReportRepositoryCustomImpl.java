package project.tripplan.domain.report.repository.reviewCommentReportRepo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.QReviewComment;
import project.tripplan.domain.report.entity.QReviewCommentReport;
import project.tripplan.domain.report.entity.ReviewCommentReport;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class ReviewCommentReportRepositoryCustomImpl implements ReviewCommentReportRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QReviewCommentReport reviewCommentReport = QReviewCommentReport.reviewCommentReport;
	private final QUser user = QUser.user;
	private final QReviewComment reviewComment = QReviewComment.reviewComment;

	@Override
	public Optional<ReviewCommentReport> findByUserIdAndReportId(Long userId, Long reportId) {
		return Optional.ofNullable(
			qf.selectFrom(reviewCommentReport)
				.join(reviewCommentReport.user, user).fetchJoin()
				.join(reviewCommentReport.reviewComment, reviewComment).fetchJoin()
				.where(user.id.eq(userId).and(reviewComment.id.eq(reportId)))
				.fetchOne()
		);
	}
}

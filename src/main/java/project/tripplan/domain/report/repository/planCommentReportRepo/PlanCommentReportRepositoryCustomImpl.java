package project.tripplan.domain.report.repository.planCommentReportRepo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.QComment;
import project.tripplan.domain.report.entity.PlanCommentReport;
import project.tripplan.domain.report.entity.QPlanCommentReport;
import project.tripplan.domain.report.entity.QPlanCommentReportReason;
import project.tripplan.domain.report.entity.QReportReason;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanCommentReportRepositoryCustomImpl implements PlanCommentReportRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlanCommentReport planCommentReport = QPlanCommentReport.planCommentReport;
	private final QUser user = QUser.user;
	private final QComment comment = QComment.comment;

	@Override
	public Optional<PlanCommentReport> findByUserIdAndCommentId(Long userId, Long commentId) {
		return Optional.ofNullable(
			qf.selectFrom(planCommentReport)
				.join(planCommentReport.user, user).fetchJoin()
				.join(planCommentReport.comment, comment).fetchJoin()
				.where(user.id.eq(userId).and(comment.id.eq(commentId)))
				.fetchOne()
		);
	}
}

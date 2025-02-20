package project.tripplan.domain.report.repository.commentReportRepo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.QComment;
import project.tripplan.domain.report.entity.CommentReport;
import project.tripplan.domain.report.entity.QCommentReport;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class CommentReportRepositoryCustomImpl implements CommentReportRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QCommentReport commentReport = QCommentReport.commentReport;
	private final QUser user = QUser.user;
	private final QComment comment = QComment.comment;

	@Override
	public Optional<CommentReport> findByUserIdAndCommentId(Long userId, Long commentId) {
		return Optional.ofNullable(
			qf.selectFrom(commentReport)
				.join(commentReport.user, user).fetchJoin()
				.join(commentReport.comment, comment).fetchJoin()
				.where(user.id.eq(userId).and(comment.id.eq(commentId)))
				.fetchOne()
		);
	}
}

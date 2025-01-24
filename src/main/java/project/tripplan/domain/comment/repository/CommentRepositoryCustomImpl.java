package project.tripplan.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.comment.entity.QComment;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QComment comment = QComment.comment;
	private final QUser user = QUser.user;
	private final QPlan plan = QPlan.plan;

	@Override
	public Optional<Comment> findByIdWithUser(Long commentId) {
		return Optional.ofNullable(
			qf.selectFrom(comment)
				.join(comment.user, user).fetchJoin()
				.where(comment.id.eq(commentId))
				.fetchOne()
		);
	}

	@Override
	public Page<Comment> findAllByPlanIdWithUser(Long planId, Pageable pageable) {
		// 쿼리 생성
		List<Comment> content = qf
			.selectFrom(comment)
			.join(comment.plan, plan).fetchJoin()
			.join(comment.user, user).fetchJoin()
			.where(plan.id.eq(planId))
			.orderBy(comment.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 총 개수 계산
		Long totalCount = qf
			.select(comment.count())
			.from(comment)
			.where(plan.id.eq(planId))
			.fetchOne();

		long total = (totalCount == null) ? 0 : totalCount;

		// PageImpl을 사용해 Page 객체 반환
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<Comment> findCommentsByUser(Long userId, Pageable pageable) {
		List<Comment> content = qf
			.selectFrom(comment)
			.join(comment.plan, plan).fetchJoin()
			.where(comment.user.id.eq(userId))
			.orderBy(comment.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long totalCount = qf
			.select(comment.count())
			.from(comment)
			.where(comment.user.id.eq(userId))
			.fetchOne();

		long total = (totalCount == null) ? 0 : totalCount;

		return new PageImpl<>(content, pageable, total);
	}
}

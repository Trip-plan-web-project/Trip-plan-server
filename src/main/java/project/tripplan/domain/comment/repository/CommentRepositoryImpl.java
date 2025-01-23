package project.tripplan.domain.comment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.comment.entity.QComment;
import project.tripplan.domain.plan.entity.QPlan;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QComment comment = QComment.comment;
	private final QPlan plan = QPlan.plan;

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

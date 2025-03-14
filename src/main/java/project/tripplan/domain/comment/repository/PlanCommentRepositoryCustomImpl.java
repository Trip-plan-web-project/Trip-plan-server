package project.tripplan.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.comment.entity.PlanComment;
import project.tripplan.domain.comment.entity.QPlanComment;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanCommentRepositoryCustomImpl implements PlanCommentRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QPlanComment planComment = QPlanComment.planComment;
	private final QUser user = QUser.user;
	private final QPlan plan = QPlan.plan;

	@Override
	public Optional<PlanComment> findByIdWithUserAndPlan(Long commentId) {
		return Optional.ofNullable(
			qf.selectFrom(planComment)
				.join(planComment.user, user).fetchJoin()
				.join(planComment.plan, plan).fetchJoin()
				.where(planComment.id.eq(commentId))
				.fetchOne()
		);
	}

	@Override
	public Page<PlanComment> findAllByPlanIdWithUser(Long planId, Pageable pageable) {
		// 쿼리 생성
		List<PlanComment> content = qf
			.selectFrom(planComment)
			.join(planComment.plan, plan).fetchJoin()
			.join(planComment.user, user).fetchJoin()
			.where(plan.id.eq(planId))
			.orderBy(planComment.createdAt.desc(), planComment.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 총 개수 계산
		Long totalCount = qf
			.select(planComment.count())
			.from(planComment)
			.where(plan.id.eq(planId))
			.fetchOne();

		long total = (totalCount == null) ? 0 : totalCount;

		// PageImpl을 사용해 Page 객체 반환
		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<PlanComment> findCommentsByUser(Long userId, Pageable pageable) {
		List<PlanComment> content = qf
			.selectFrom(planComment)
			.join(planComment.plan, plan).fetchJoin()
			.where(planComment.user.id.eq(userId))
			.orderBy(planComment.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long totalCount = qf
			.select(planComment.count())
			.from(planComment)
			.where(planComment.user.id.eq(userId))
			.fetchOne();

		long total = (totalCount == null) ? 0 : totalCount;

		return new PageImpl<>(content, pageable, total);
	}
}

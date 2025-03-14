package project.tripplan.domain.like.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.like.entity.PlanLike;
import project.tripplan.domain.like.entity.QPlanLike;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanLikeRepositoryCustomImpl implements PlanLikeRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlanLike planLike = QPlanLike.planLike;
	private final QUser user = QUser.user;
	private final QPlan plan = QPlan.plan;

	@Override
	public Long countLikesByPlanId(Long planId) {
		return qf.select(planLike.count())
			.from(planLike)
			.where(planLike.plan.id.eq(planId))
			.fetchOne();
	}

	@Override
	public Optional<PlanLike> findPlanLikeWithUserAndPlan(Long planLikeId) {
		return Optional.ofNullable(
			qf.selectFrom(planLike)
				.join(planLike.user, user).fetchJoin()
				.join(planLike.plan, plan).fetchJoin()
				.where(planLike.id.eq(planLikeId))
				.fetchOne()
		);
	}

	@Override
	public Optional<PlanLike> findPlanLikeWithUserAndPlan(Long userId, Long planId) {
		return Optional.ofNullable(
			qf.selectFrom(planLike)
				.join(planLike.user, user).fetchJoin()
				.join(planLike.plan, plan).fetchJoin()
				.where(planLike.user.id.eq(userId).and(planLike.plan.id.eq(planId)))
				.fetchOne()
		);
	}
}

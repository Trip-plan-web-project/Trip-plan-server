package project.tripplan.domain.planLike.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.planLike.entity.QPlanLike;

@Repository
@RequiredArgsConstructor
public class PlanLikeRepositoryCustomImpl implements PlanLikeRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlanLike planLike = QPlanLike.planLike;

	@Override
	public Long countLikesByPlanId(Long planId) {
		return qf.select(planLike.count())
			.from(planLike)
			.where(planLike.plan.id.eq(planId))
			.fetchOne();
	}
}

package project.tripplan.domain.planLike.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.planLike.entity.PlanLike;
import project.tripplan.domain.planLike.entity.QPlanLike;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanLikeRepositoryCustomImpl implements PlanLikeRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlanLike planLike = QPlanLike.planLike;
	private final QUser user = QUser.user;

	@Override
	public Long countLikesByPlanId(Long planId) {
		return qf.select(planLike.count())
			.from(planLike)
			.where(planLike.plan.id.eq(planId))
			.fetchOne();
	}

	@Override
	public Optional<PlanLike> findPlanLikeWithUser(Long planLikeId) {
		return Optional.ofNullable(
			qf.selectFrom(planLike)
				.join(planLike.user, user).fetchJoin()
				.where(planLike.id.eq(planLikeId))
				.fetchOne()
		);
	}
}

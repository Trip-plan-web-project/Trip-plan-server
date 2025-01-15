package project.tripplan.domain.plan.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanRepositoryCustomImpl implements PlanRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlan plan = QPlan.plan;
	private final QUser user = QUser.user;

	@Override
	public Optional<Plan> findByPlanIdWithUser(Long planId) {
		return Optional.ofNullable(
			qf.selectFrom(plan)
				.join(plan.user, user).fetchJoin()
				.where(plan.id.eq(planId))
				.fetchOne()
		);
	}
}

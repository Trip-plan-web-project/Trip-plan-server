package project.tripplan.domain.plan.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.category.placeCategory.entity.QPlaceCategory;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.plan.entity.QPlanPlaceCategory;
import project.tripplan.domain.plan.enums.PlanStatus;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PlanPlaceCategoryRepositoryCustomImpl implements PlanPlaceCategoryRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlan plan = QPlan.plan;
	private final QUser user = QUser.user;
	private final QPlanPlaceCategory planPlaceCategory = QPlanPlaceCategory.planPlaceCategory;
	private final QPlaceCategory placeCategory = QPlaceCategory.placeCategory;

	@Override
	public List<PlanPlaceCategory> findAllByPlanIdWithPlanAndPlace(Long planId) {
		return qf.selectFrom(planPlaceCategory)
			.join(planPlaceCategory.plan, plan).fetchJoin()
			.join(planPlaceCategory.placeCategory, placeCategory).fetchJoin()
			.where(planPlaceCategory.plan.id.eq(planId))
			.fetch();
	}

	@Override
	public List<PlanPlaceCategory> findAllByPlanIds(List<Long> planIds) {
		return qf.selectFrom(planPlaceCategory)
			.join(planPlaceCategory.placeCategory, placeCategory).fetchJoin()
			.join(planPlaceCategory.plan, plan).fetchJoin()
			.where(plan.id.in(planIds))
			.fetch();
	}

	@Override
	public List<PlanPlaceCategory> findHotPlacesByPlaceName(String placeName, int limit) {
		return qf.selectFrom(planPlaceCategory)
			.join(planPlaceCategory.placeCategory, placeCategory).fetchJoin()
			.join(planPlaceCategory.plan, plan).fetchJoin()
			.where(placeCategory.name.eq(placeName)
				.and(planPlaceCategory.plan.status.eq(PlanStatus.PUBLIC)))
			.orderBy(plan.createdAt.desc(),plan.createdAt.desc())
			.limit(limit)
			.fetch();
	}

}

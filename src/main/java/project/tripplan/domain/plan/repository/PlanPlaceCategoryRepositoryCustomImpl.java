package project.tripplan.domain.plan.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.category.placeCategory.entity.QPlaceCategory;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.plan.entity.QPlanPlaceCategory;

@Repository
@RequiredArgsConstructor
public class PlanPlaceCategoryRepositoryCustomImpl implements PlanPlaceCategoryRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlan plan = QPlan.plan;
	private final QPlanPlaceCategory planPlaceCategory = QPlanPlaceCategory.planPlaceCategory;
	private final QPlaceCategory placeCategory = QPlaceCategory.placeCategory;

	@Override
	public List<PlanPlaceCategory> findAllByPlanIdWithPlanAndPlace(Long planId) {
		return qf.select(planPlaceCategory)
			.from(planPlaceCategory)
			.join(planPlaceCategory.plan, plan).fetchJoin()
			.join(planPlaceCategory.placeCategory, placeCategory).fetchJoin()
			.where(planPlaceCategory.plan.id.eq(planId))
			.fetch();
	}
}

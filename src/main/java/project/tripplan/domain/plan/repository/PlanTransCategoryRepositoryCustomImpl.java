package project.tripplan.domain.plan.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.category.transportationCategory.entitiy.QTransportationCategory;
import project.tripplan.domain.plan.entity.PlanTransportationCategory;
import project.tripplan.domain.plan.entity.QPlanTransportationCategory;

@Repository
@RequiredArgsConstructor
public class PlanTransCategoryRepositoryCustomImpl implements PlanTransCategoryRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlanTransportationCategory planTransCategory = QPlanTransportationCategory.planTransportationCategory;
	private final QTransportationCategory transCategory = QTransportationCategory.transportationCategory;

	@Override
	public Optional<PlanTransportationCategory> findByPlanIdWithPlanTransCategory(Long planId) {
		return Optional.ofNullable(
			qf.selectFrom(planTransCategory)
				.join(planTransCategory.transportationCategory, transCategory).fetchJoin()
				.where(planTransCategory.plan.id.eq(planId))
				.fetchOne()
		);
	}
}

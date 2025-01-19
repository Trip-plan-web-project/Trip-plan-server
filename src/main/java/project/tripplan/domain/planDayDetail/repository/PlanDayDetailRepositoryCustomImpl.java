package project.tripplan.domain.planDayDetail.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.category.planCategory.entity.QPlanCategory;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.planDay.entity.QPlanDay;
import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;
import project.tripplan.domain.planDayDetail.entity.QPlanDayDetail;

@Repository
@RequiredArgsConstructor
public class PlanDayDetailRepositoryCustomImpl implements PlanDayDetailRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlanDayDetail planDayDetail = QPlanDayDetail.planDayDetail;
	private final QPlanDay planDay = QPlanDay.planDay;
	private final QPlanCategory planCategory = QPlanCategory.planCategory;
	private final QPlan plan = QPlan.plan;

	@Override
	public List<PlanDayDetail> findAllByPlanIdWithPlanCategory(Long planId) {
		return qf.selectFrom(planDayDetail)
			.join(planDayDetail.planDay, planDay).fetchJoin()
			.join(planDay.plan, plan).fetchJoin()
			.join(planDayDetail.planCategory, planCategory).fetchJoin()
			.where(planDayDetail.planDay.plan.id.eq(planId))
			.fetch();
	}
}

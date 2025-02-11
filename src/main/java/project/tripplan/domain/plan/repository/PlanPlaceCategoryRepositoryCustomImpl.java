package project.tripplan.domain.plan.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.category.placeCategory.entity.QPlaceCategory;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.plan.entity.QPlanPlaceCategory;
import project.tripplan.domain.plan.enums.PlanStatus;
import project.tripplan.domain.user.dto.UserPlansDraftsRes;
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

	@Override
	public Page<UserPlansDraftsRes> findAllByUserIdWithPlan(Long userId, Pageable pageable) {
		// 1. QueryDSL에서 DTO로 변환하는 쿼리
		List<UserPlansDraftsRes> content = qf
			.select(Projections.constructor(UserPlansDraftsRes.class,
				plan.id.as("planId"),
				plan.title,
				plan.imageUrl,
				plan.createdAt,
				Expressions.stringTemplate("group_concat({0})", placeCategory.name).as("categories") // 카테고리 리스트로 묶기
			))
			.from(planPlaceCategory)
			.join(planPlaceCategory.plan, plan)
			.join(planPlaceCategory.plan.user, user)
			.join(planPlaceCategory.placeCategory, placeCategory)
			.where(planPlaceCategory.plan.user.id.eq(userId)
				.and(planPlaceCategory.plan.status.eq(PlanStatus.TEMPORARY_STORAGE)))
			.groupBy(plan.id)  // 같은 planId 기준으로 그룹화
			.orderBy(plan.createdAt.desc(), plan.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 2. count 쿼리 실행
		Long totalCount = qf
			.select(plan.count())
			.from(plan)
			.where(plan.user.id.eq(userId)
				.and(plan.status.eq(PlanStatus.TEMPORARY_STORAGE)))
			.fetchOne();

		long total = (totalCount == null) ? 0 : totalCount;

		return new PageImpl<>(content, pageable, total);
	}

}

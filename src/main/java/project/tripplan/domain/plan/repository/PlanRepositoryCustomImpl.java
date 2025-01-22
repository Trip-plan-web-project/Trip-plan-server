package project.tripplan.domain.plan.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.category.placeCategory.entity.QPlaceCategory;
import project.tripplan.domain.category.transportationCategory.entitiy.QTransportationCategory;
import project.tripplan.domain.category.transportationCategory.enums.TransportationName;
import project.tripplan.domain.plan.dto.PlanNoOffsetReq;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.plan.entity.QPlanPlaceCategory;
import project.tripplan.domain.plan.entity.QPlanTransportationCategory;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.entity.QUser;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PlanRepositoryCustomImpl implements PlanRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlan plan = QPlan.plan;
	private final QUser user = QUser.user;
	private final QTransportationCategory transportationCategory = QTransportationCategory.transportationCategory;
	private final QPlanPlaceCategory planPlaceCategory = QPlanPlaceCategory.planPlaceCategory;
	private final QPlanTransportationCategory planTransport = QPlanTransportationCategory.planTransportationCategory;
	private final QPlaceCategory placeCategory = QPlaceCategory.placeCategory;

	@Override
	public Optional<Plan> findByPlanIdWithUser(Long planId) {

		return Optional.ofNullable(
			qf.selectFrom(plan)
				.join(plan.user, user).fetchJoin()
				.where(plan.id.eq(planId))
				.fetchOne());
	}

	@Override
	public List<Plan> searchPlanNoOffset(PlanNoOffsetReq req) {
		// (A) limit
		int limit = req.getSize() + 1;

		// (B) 필터링 조건
		BooleanBuilder builder = buildSearchCondition(req);

		// (C) 쿼리 생성 (중복 Plan 제거 위해 distinct)
		JPAQuery<Plan> query = qf
			.selectDistinct(plan)
			.from(plan)
			.leftJoin(plan.planPlaceCategories, planPlaceCategory).fetchJoin()
			.leftJoin(planPlaceCategory.placeCategory, placeCategory).fetchJoin()
			.leftJoin(plan.planTransportationCategories, planTransport).fetchJoin()
			.leftJoin(planTransport.transportationCategory, transportationCategory).fetchJoin()
			.where(builder);

		// (D) No-Offset 커서 처리 + ORDER BY
		applyNoOffset(query, req);

		// (E) limit
		query.limit(limit);

		// (F) fetch
		return query.fetch();
	}

	private BooleanBuilder buildSearchCondition(PlanNoOffsetReq req) {
		BooleanBuilder builder = new BooleanBuilder();

		// 1) 카테고리 ID in (OR 조건)
		if (req.getCategoryIds() != null && !req.getCategoryIds().isEmpty()) {
			builder.and(placeCategory.id.in(req.getCategoryIds()));
		}

		// 2) day
		if (req.getDay() != null && req.getDay() > 0) {
			builder.and(
				Expressions.numberTemplate(Integer.class, "DATEDIFF({0}, {1})", plan.endDate, plan.startDate)
					.add(1)
					.eq(req.getDay())
			);
		}

		// 3) 교통수단
		if (req.getTransportCategoryName() != null && !req.getTransportCategoryName().isEmpty()) {
			TransportationName enumValue = TransportationName.valueOf(req.getTransportCategoryName());
			builder.and(transportationCategory.name.eq(enumValue));
		}

		// 4) 인원
		if (req.getPeople() != null && req.getPeople() > 0) {
			builder.and(plan.people.eq(req.getPeople()));
		}

		return builder;
	}

	private void applyNoOffset(JPAQuery<Plan> query, PlanNoOffsetReq req) {
		String sortBy = (req.getSortBy() != null) ? req.getSortBy() : "id";
		String direction = (req.getDirection() != null) ? req.getDirection().toUpperCase() : "DESC";
		String lastValue = req.getLastValue();
		Long lastId = req.getLastId();

		// (A) 커서 조건
		if (lastValue != null && lastId != null) {
			switch (sortBy) {
				case "viewCount":
					long lastViewCount = Long.parseLong(lastValue);
					if ("DESC".equals(direction)) {
						// viewCount < lastViewCount OR (== and plan.id < lastId)
						query.where(
							plan.viewCount.lt(lastViewCount)
								.or(
									plan.viewCount.eq(lastViewCount)
										.and(plan.id.lt(lastId))
								)
						);
					} else {
						// viewCount > lastViewCount OR (== and plan.id > lastId)
						query.where(
							plan.viewCount.gt(lastViewCount)
								.or(
									plan.viewCount.eq(lastViewCount)
										.and(plan.id.gt(lastId))
								)
						);
					}
					break;

				case "id":
				default:
					long lastPk = Long.parseLong(lastValue);
					if ("DESC".equals(direction)) {
						query.where(plan.id.lt(lastPk));
					} else {
						query.where(plan.id.gt(lastPk));
					}
					break;
			}
		}

		// (B) ORDER BY
		switch (sortBy) {
			case "viewCount":
				if ("DESC".equals(direction)) {
					// viewCount DESC, id DESC
					query.orderBy(plan.viewCount.desc(), plan.id.desc());
				} else {
					// viewCount ASC, id ASC
					query.orderBy(plan.viewCount.asc(), plan.id.asc());
				}
				break;

			case "id":
			default:
				if ("DESC".equals(direction)) {
					query.orderBy(plan.id.desc());
				} else {
					query.orderBy(plan.id.asc());
				}
				break;
		}
	}

	@Override
	public List<Plan> findMostViewedPlans(int limit) {
		// return qf.selectFrom(plan)
		// 	.join(plan.)
		return Collections.emptyList();
	}

	@Override
	public List<Plan> findMostRecentPlans(int limit) {
		return List.of();
	}

	@Override
	public List<Plan> findHotPlacePlans(String placeName, int limit) {
		return List.of();
	}

	@Override
	public Page<UserPlanRes> findPlansByUserId(Long userId, Pageable pageable) {
		List<UserPlanRes> content = qf
			.select(Projections.constructor(
				UserPlanRes.class,
				plan.id,
				plan.title,
				plan.createdAt,
				plan.imageUrl.as("thumbnail"),
				Expressions.stringTemplate(
					"group_concat(DISTINCT {0})",
					planPlaceCategory.placeCategory.name
				)
				,
				plan.status.stringValue()
			))
			.from(plan)
			.leftJoin(plan.planPlaceCategories, planPlaceCategory)
			.where(plan.user.id.eq(userId))
			.orderBy(plan.createdAt.desc())
			.groupBy(plan.id)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = qf
			.select(plan.count())
			.from(plan)
			.where(plan.user.id.eq(userId))
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

}

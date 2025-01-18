package project.tripplan.domain.plan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
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
import project.tripplan.domain.user.entity.QUser;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PlanRepositoryCustomImpl implements PlanRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPlan plan = QPlan.plan;
	private final QUser user = QUser.user;
	private final QPlanTransportationCategory planTransportationCategory = QPlanTransportationCategory.planTransportationCategory;
	private final QTransportationCategory transportationCategory = QTransportationCategory.transportationCategory;
	private final QPlanPlaceCategory planPlaceCategory = QPlanPlaceCategory.planPlaceCategory;
	private final QPlanTransportationCategory planTransport = QPlanTransportationCategory.planTransportationCategory;

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
		// 1) size+1
		int limit = req.getSize() + 1;

		// 2) 검색 조건 (카테고리, day, people, transportCategoryName 등)
		BooleanBuilder builder = buildSearchCondition(req);

		// 3) 쿼리 생성 + 필요한 JOIN
		JPAQuery<Plan> query = qf
			.selectFrom(plan)
			.leftJoin(plan.planPlaceCategories, planPlaceCategory).fetchJoin()
			.leftJoin(planPlaceCategory.placeCategory, QPlaceCategory.placeCategory).fetchJoin()
			.leftJoin(plan.planTransportationCategories, planTransport).fetchJoin()
			.leftJoin(planTransport.transportationCategory, transportationCategory).fetchJoin()
			.where(builder);

		// 4) 정렬/커서 처리
		applyNoOffset(query, req);

		// 5) limit
		query.limit(limit);

		// 6) fetch
		return query.fetch();
	}

	/**
	 * 검색 조건 (카테고리, day, people, transportCategory)
	 */
	private BooleanBuilder buildSearchCondition(PlanNoOffsetReq req) {
		BooleanBuilder builder = new BooleanBuilder();

		// 1) categoryIds 가 있다면
		if (req.getCategoryIds() != null && !req.getCategoryIds().isEmpty()) {
			builder.and(QPlaceCategory.placeCategory.id.in(req.getCategoryIds()));
		}

		// day
		if (req.getDay() != null && req.getDay() > 0) {
			// DATEDIFF(endDate, startDate)+1 = day
			builder.and(
				Expressions.numberTemplate(Integer.class, "DATEDIFF({0}, {1})", plan.endDate, plan.startDate)
					.add(1)
					.eq(req.getDay())
			);
		}

		// transportCategoryName
		if (req.getTransportCategoryName() != null && !req.getTransportCategoryName().isEmpty()) {
			TransportationName enumValue = TransportationName.valueOf(req.getTransportCategoryName());
			builder.and(transportationCategory.name.eq(enumValue));
		}

		// people
		if (req.getPeople() != null && req.getPeople() > 0) {
			builder.and(plan.people.eq(req.getPeople()));
		}

		// ★ 여기에서는 lastId( plan.id < lastId )는 적용 X
		//   -> 정렬이 id일 때는 applyNoOffset()에서 처리.

		return builder;
	}

	/**
	 * 정렬/커서(No-Offset) 로직
	 */
	private void applyNoOffset(JPAQuery<Plan> query, PlanNoOffsetReq req) {
		String sortBy = req.getSortBy() != null ? req.getSortBy().toLowerCase() : "id";
		String direction = req.getDirection() != null ? req.getDirection().toUpperCase() : "DESC";
		String lastValue = req.getLastValue();
		Long lastId = req.getLastId();

		// (A) WHERE 커서 조건
		if (lastValue != null && lastId != null) {
			switch (sortBy) {
				case "viewCount":
					long lastViewCount = Long.parseLong(lastValue);
					if ("DESC".equals(direction)) {
						query.where(
							plan.viewCount.lt(lastViewCount)
								.or(plan.viewCount.eq(lastViewCount)
									.and(plan.id.lt(lastId)))
						);
					} else {
						query.where(
							plan.viewCount.gt(lastViewCount)
								.or(plan.viewCount.eq(lastViewCount)
									.and(plan.id.gt(lastId)))
						);
					}
					break;
				case "id":
				default:
					// id 정렬
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
					query.orderBy(plan.viewCount.desc());
				} else {
					query.orderBy(plan.viewCount.asc());
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
}

package project.tripplan.domain.plan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import project.tripplan.domain.plan.dto.PlanSearchReq;
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

	@Override
	public Optional<Plan> findByPlanIdWithUser(Long planId) {

		return Optional.ofNullable(
			qf.selectFrom(plan)
				.join(plan.user, user).fetchJoin()
				.where(plan.id.eq(planId))
				.fetchOne()
		);
	}

	@Override
	public Page<Plan> searchPlan(PlanSearchReq req, Pageable pageable) {

		BooleanBuilder builder = buildSearchCondition(req);

		// 조회용 쿼리
		JPAQuery<Plan> contentQuery = qf
			.selectFrom(plan)
			.leftJoin(plan.planTransportationCategories, planTransportationCategory).fetchJoin()
			.leftJoin(planTransportationCategory.transportationCategory, transportationCategory).fetchJoin()
			.leftJoin(plan.planPlaceCategories, planPlaceCategory).fetchJoin()
			.leftJoin(planPlaceCategory.placeCategory, QPlaceCategory.placeCategory).fetchJoin()
			.where(builder);

		// 정렬
		applySorting(contentQuery, pageable);

		// 페이징
		List<Plan> content = contentQuery
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// count 쿼리
		long totalCount = qf
			.select(plan.count())
			.from(plan)
			.leftJoin(plan.planTransportationCategories, planTransportationCategory)
			.leftJoin(planTransportationCategory.transportationCategory, transportationCategory)
			.leftJoin(plan.planPlaceCategories, planPlaceCategory)
			.leftJoin(planPlaceCategory.placeCategory, QPlaceCategory.placeCategory)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(content, pageable, totalCount);
	}

	private BooleanBuilder buildSearchCondition(PlanSearchReq req) {
		BooleanBuilder builder = new BooleanBuilder();
		categoryNameEq(req, builder);
		dayEq(req, builder);
		transportCategoryNameEq(req, builder);
		peopleEq(req, builder);
		return builder;
	}

	private void peopleEq(PlanSearchReq req, BooleanBuilder builder) {
		if (req.getPeople() != null && req.getPeople() > 0) {
			builder.and(plan.people.eq(req.getPeople()));
		}
	}

	private void transportCategoryNameEq(PlanSearchReq req, BooleanBuilder builder) {
		if (req.getTransportCategoryName() != null && !req.getTransportCategoryName().isEmpty()) {
			TransportationName enumValue = TransportationName.valueOf(req.getTransportCategoryName());
			builder.and(transportationCategory.name.eq(enumValue));
		}
	}

	private void dayEq(PlanSearchReq req, BooleanBuilder builder) {
		if (req.getDay() != null && req.getDay() > 0) {
			builder.and(
				Expressions.numberTemplate(Integer.class, "DATEDIFF({0}, {1})", plan.endDate, plan.startDate)
					.add(1)
					.eq(req.getDay())
			);
		}
	}

	private static void categoryNameEq(PlanSearchReq req, BooleanBuilder builder) {
		if (req.getCategoryNames() != null && !req.getCategoryNames().isEmpty()) {
			builder.and(QPlaceCategory.placeCategory.name.in(req.getCategoryNames()));
		}
	}

	private void applySorting(JPAQuery<Plan> contentQuery, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			String property = order.getProperty();
			boolean asc = order.isAscending();

			switch (property) {
				case "title":
					contentQuery.orderBy(asc ? plan.title.asc() : plan.title.desc());
					break;
				case "viewCount":
					contentQuery.orderBy(asc ? plan.viewCount.asc() : plan.viewCount.desc());
					break;
				case "people":
					contentQuery.orderBy(asc ? plan.people.asc() : plan.people.desc());
					break;
				default:
					contentQuery.orderBy(asc ? plan.id.asc() : plan.id.desc());
					break;
			}
		}
	}
}

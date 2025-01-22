package project.tripplan.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.plan.entity.QPlanPlaceCategory;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.entity.QUser;
import project.tripplan.domain.user.entity.User;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QUser user = QUser.user;
	private final QPlan plan = QPlan.plan;
	private final QPlanPlaceCategory placeCategory = QPlanPlaceCategory.planPlaceCategory;

	@Override
	public Optional<User> findBySocialId(String socialId) {
		return Optional.ofNullable(
			qf.selectFrom(user)
				.where(user.socialId.eq(socialId))
				.fetchOne()
		);
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
					placeCategory.placeCategory.name
				)
				,
				plan.status.stringValue()
			))
			.from(plan)
			.leftJoin(plan.planPlaceCategories, placeCategory)
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

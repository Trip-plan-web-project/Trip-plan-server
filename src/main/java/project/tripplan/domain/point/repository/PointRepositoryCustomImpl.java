package project.tripplan.domain.point.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.admin.dto.PointHistoryRes;
import project.tripplan.domain.point.entity.Point;
import project.tripplan.domain.point.entity.QPoint;
import project.tripplan.domain.point.enums.PointStatus;
import project.tripplan.domain.point.enums.PointType;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class PointRepositoryCustomImpl implements PointRepositoryCustom {

	private final JPAQueryFactory qf;
	private final QPoint point = QPoint.point1;
	private final QUser user = QUser.user;

	@Override
	public Page<PointHistoryRes> findPointHistory(Pageable pageable, Integer category, String startDate,
		String endDate) {
		BooleanBuilder conditions = findHistoryConditions(category, startDate, endDate);

		List<PointHistoryRes> results = qf.select(Projections.constructor(PointHistoryRes.class,
				point.id,
				point.user.nickname,
				point.pointType,
				point.point,
				point.pointTypeId,
				point.pointStatus,
				point.createdAt
			))
			.from(point)
			.join(point.user, user)
			.where(conditions)
			.orderBy(point.createdAt.desc(), point.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = Optional.ofNullable(
			qf.select(point.count())
				.from(point)
				.where(conditions)
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(results, pageable, total);
	}

	private BooleanBuilder findHistoryConditions(Integer category, String startDate,
		String endDate) {
		BooleanBuilder conditions = new BooleanBuilder();

		if(category != null) {
			switch (category) {
				case 2: // 일정작성 적립 목록
					conditions.and(point.pointType.eq(PointType.PLAN));
					break;
				case 3: // 리뷰작성 적립 목록
					conditions.and(point.pointType.eq(PointType.REVIEW));
					break;
				default:
					break;
			}
		}

		if(startDate != null && endDate != null) {
			LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
			LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
			conditions.and(point.createdAt.between(start, end));
		}

		return conditions;
	}

	@Override
	public List<Point> findAllByIdWithUserIds(List<Long> ids) {
		return qf.selectFrom(point)
			.join(point.user, user).fetchJoin()
			.where(point.id.in(ids))
			.fetch();
	}

	@Override
	public Page<Point> findAllWithUser(Pageable pageable, Long userId) {
		List<Point> results = qf.selectFrom(point)
			.join(point.user, user).fetchJoin()
			.where(user.id.eq(userId).and(point.pointStatus.eq(PointStatus.COMPLETED)))
			.orderBy(point.updatedAt.desc(), point.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = Optional.ofNullable(
			qf.select(point.count())
				.from(point)
				.where(point.user.id.eq(userId))
				.fetchOne()
		).orElse(0L);

		return new PageImpl<>(results, pageable, total);
	}
}

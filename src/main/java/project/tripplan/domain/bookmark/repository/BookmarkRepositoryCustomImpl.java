package project.tripplan.domain.bookmark.repository;

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
import project.tripplan.domain.bookmark.entity.Bookmark;
import project.tripplan.domain.bookmark.entity.QBookmark;
import project.tripplan.domain.plan.entity.QPlan;
import project.tripplan.domain.plan.entity.QPlanPlaceCategory;
import project.tripplan.domain.user.dto.UserBookmarkRes;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QBookmark bookmark = QBookmark.bookmark;
	private final QUser user = QUser.user;
	private final QPlanPlaceCategory placeCategory = QPlanPlaceCategory.planPlaceCategory;
	private final QPlan plan = QPlan.plan;

	@Override
	public Optional<Bookmark> findByBookmarkIdWithUserId(Long bookmarkId, Long userId) {
		return Optional.ofNullable(
			qf.selectFrom(bookmark)
				.join(bookmark.user, user).fetchJoin()
				.where(bookmark.user.id.eq(userId)
					.and(bookmark.id.eq(bookmarkId)))
				.fetchOne()
		);
	}

	@Override
	public Page<UserBookmarkRes> findBookmarksByUserId(Long userId, Pageable pageable) {
		List<UserBookmarkRes> content = qf
			.select(Projections.constructor(
				UserBookmarkRes.class,
				bookmark.id,
				plan.id,
				plan.title,
				plan.imageUrl.as("thumbnail"),
				Expressions.stringTemplate(
					"group_concat(DISTINCT {0})",
					placeCategory.placeCategory.name
				)
			))
			.from(bookmark)
			.join(bookmark.plan, plan)
			.leftJoin(plan.planPlaceCategories, placeCategory)
			.where(bookmark.user.id.eq(userId))
			.groupBy(bookmark.id, plan.id)
			.orderBy(bookmark.createdAt.desc())
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

	@Override
	public Optional<Bookmark> findByUserAndPlan(Long userId, Long planId) {
		return Optional.ofNullable(
			qf.selectFrom(bookmark)
				.join(bookmark.user, user).fetchJoin()
				.join(bookmark.plan, plan).fetchJoin()
				.where(bookmark.user.id.eq(userId)
					.and(bookmark.plan.id.eq(planId)))
				.fetchOne()
		);
	}
}

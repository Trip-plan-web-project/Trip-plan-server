package project.tripplan.domain.bookmark.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.bookmark.entity.Bookmark;
import project.tripplan.domain.bookmark.entity.QBookmark;
import project.tripplan.domain.user.entity.QUser;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QBookmark bookmark = QBookmark.bookmark;
	private final QUser user = QUser.user;

	@Override
	public Optional<Bookmark> findByBookmarkIdWithUser(Long bookmarkId, Long userId) {
		return Optional.ofNullable(
			qf.selectFrom(bookmark)
				.join(bookmark.user, user).fetchJoin()
				.where(bookmark.user.id.eq(userId)
					.and(bookmark.id.eq(bookmarkId)))
				.fetchOne()
		);
	}
}

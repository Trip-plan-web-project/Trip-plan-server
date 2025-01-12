package project.tripplan.domain.bookmark.repository;

import java.util.Optional;

import project.tripplan.domain.bookmark.entity.Bookmark;

public interface BookmarkRepositoryCustom {
	Optional<Bookmark> findByBookmarkIdWithUser(Long bookmarkId, Long userId);
}

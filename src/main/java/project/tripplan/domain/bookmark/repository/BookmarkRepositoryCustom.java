package project.tripplan.domain.bookmark.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.bookmark.entity.Bookmark;
import project.tripplan.domain.user.dto.UserBookmarkRes;

public interface BookmarkRepositoryCustom {
	Optional<Bookmark> findByBookmarkIdWithUser(Long bookmarkId, Long userId);

	Page<UserBookmarkRes> findBookmarksByUserId(Long userId, Pageable pageable);
}

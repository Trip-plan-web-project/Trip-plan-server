package project.tripplan.domain.bookmark.repository;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.bookmark.entity.Bookmark;
import project.tripplan.domain.user.dto.UserBookmarkRes;

public interface BookmarkRepositoryCustom {
	Optional<Bookmark> findByBookmarkIdWithUserId(Long bookmarkId, Long userId);

	Page<UserBookmarkRes> findBookmarksByUserId(Long userId, Pageable pageable);

	Optional<Bookmark> findByUserAndPlan(Long userId, Long planId);
}

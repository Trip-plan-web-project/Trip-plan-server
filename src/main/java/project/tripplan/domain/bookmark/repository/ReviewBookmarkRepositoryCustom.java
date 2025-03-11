package project.tripplan.domain.bookmark.repository;

import java.util.Optional;

import project.tripplan.domain.bookmark.entity.ReviewBookmark;

public interface ReviewBookmarkRepositoryCustom {
	Optional<ReviewBookmark> findByReviewBookmarkIdWithUserId(Long reviewBookmarkId, Long userId);
}

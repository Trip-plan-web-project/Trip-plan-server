package project.tripplan.domain.bookmark.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.bookmark.entity.ReviewBookmark;

public interface ReviewBookmarkRepositoryCustom {
	Optional<ReviewBookmark> findByReviewBookmarkIdWithUserId(Long reviewBookmarkId, Long userId);

	Optional<ReviewBookmark> findByReviewIdAndUserId(Long reviewId, Long userId);

	Page<ReviewBookmark> findReviewBookmarksByUserId(Long userId, Pageable pageable);
}

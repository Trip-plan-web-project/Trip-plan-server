package project.tripplan.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.comment.entity.ReviewComment;

public interface ReviewCommentRepositoryCustom {

	Optional<ReviewComment> findByIdWithUser(Long commentId);

	Page<ReviewComment> findAllByReviewIdWithUser(Long reviewId, Pageable pageable);
}

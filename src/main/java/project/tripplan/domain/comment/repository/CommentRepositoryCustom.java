package project.tripplan.domain.comment.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.comment.entity.Comment;

public interface CommentRepositoryCustom {
  
	Optional<Comment> findByIdWithUser(Long commentId);

	Page<Comment> findAllByPlanIdWithUser(Long planId, Pageable pageable);

	Page<Comment> findCommentsByUser(Long userId, Pageable pageable);
}

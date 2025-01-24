package project.tripplan.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.comment.entity.Comment;

public interface CommentRepositoryCustom {
	Page<Comment> findCommentsByUser(Long userId, Pageable pageable);
}

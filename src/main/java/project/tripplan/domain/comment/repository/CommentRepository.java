package project.tripplan.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

}

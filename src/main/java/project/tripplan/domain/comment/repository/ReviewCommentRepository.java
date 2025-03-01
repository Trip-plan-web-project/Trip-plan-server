package project.tripplan.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.comment.entity.ReviewComment;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

}

package project.tripplan.domain.comment.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.comment.entity.PlanComment;

public interface PlanCommentRepositoryCustom {
  
	Optional<PlanComment> findByIdWithUser(Long commentId);

	Page<PlanComment> findAllByPlanIdWithUser(Long planId, Pageable pageable);

	Page<PlanComment> findCommentsByUser(Long userId, Pageable pageable);
}

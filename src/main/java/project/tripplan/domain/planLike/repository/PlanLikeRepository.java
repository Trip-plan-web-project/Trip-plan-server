package project.tripplan.domain.planLike.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.planLike.entity.PlanLike;

public interface PlanLikeRepository extends JpaRepository<PlanLike, Long> {
}

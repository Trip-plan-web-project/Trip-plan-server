package project.tripplan.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.like.entity.PlanLike;

public interface PlanLikeRepository extends JpaRepository<PlanLike, Long> {
}

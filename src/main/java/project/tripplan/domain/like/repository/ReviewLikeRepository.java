package project.tripplan.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.like.entity.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

}

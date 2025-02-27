package project.tripplan.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}

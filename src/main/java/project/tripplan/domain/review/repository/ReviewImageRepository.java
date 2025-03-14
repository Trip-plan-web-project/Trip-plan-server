package project.tripplan.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.review.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

}

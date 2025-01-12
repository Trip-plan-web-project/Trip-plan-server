package project.tripplan.domain.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;

public interface PlanPlaceCategoryRepository extends JpaRepository<PlanPlaceCategory, Long> {

}
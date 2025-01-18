package project.tripplan.domain.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tripplan.domain.plan.entity.PlanTransportationCategory;

@Repository
public interface PlanTransportationCategoryRepository extends JpaRepository<PlanTransportationCategory, Long> {
}

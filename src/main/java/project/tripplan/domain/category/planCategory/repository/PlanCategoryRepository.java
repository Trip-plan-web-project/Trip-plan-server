package project.tripplan.domain.category.planCategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.tripplan.domain.category.planCategory.entity.PlanCategory;

public interface PlanCategoryRepository extends JpaRepository<PlanCategory, Long> {
    
}

package project.tripplan.domain.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.tripplan.domain.plan.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {

}

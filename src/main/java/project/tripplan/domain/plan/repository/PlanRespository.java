package project.tripplan.domain.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.tripplan.domain.plan.entity.Plan;

@Repository
public interface PlanRespository extends JpaRepository<Plan, Long> {

}

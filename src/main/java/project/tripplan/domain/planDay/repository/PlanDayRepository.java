package project.tripplan.domain.planDay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.tripplan.domain.planDay.entity.PlanDay;

public interface PlanDayRepository extends JpaRepository<PlanDay, Long> {

}

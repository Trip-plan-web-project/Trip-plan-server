package project.tripplan.domain.planDayDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.tripplan.domain.planDayDetail.entity.PlanDayDetail;

public interface PlanDayDetailRepository extends JpaRepository<PlanDayDetail, Long> {

}

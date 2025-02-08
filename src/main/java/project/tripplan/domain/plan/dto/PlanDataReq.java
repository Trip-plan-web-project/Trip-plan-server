package project.tripplan.domain.plan.dto;

import java.time.LocalDate;
import java.util.List;

public interface PlanDataReq {
	String getTitle();

	String getTransportation();

	List<PlaceCategoryNamesReq> getCategory();

	Integer getPeople();

	LocalDate getStartDate();

	LocalDate getEndDate();

	List<PlanDayReq> getDays();
}

package project.tripplan.domain.plan.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import project.tripplan.domain.plan.enums.PlanStatus;

@Getter
public class PlanUpdateReq implements PlanDataReq {

	@NotNull
	private Long planId;

	@NotNull
	private PlanStatus status;

	@NotEmpty
	private String title;

	@NotEmpty
	private String transportation;

	@NotEmpty
	private List<PlaceCategoryNamesReq> category;

	private Integer people;

	@NotNull
	private LocalDate startDate;

	@NotNull
	private LocalDate endDate;

	@Valid
	private List<PlanDayReq> days;
}

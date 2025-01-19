package project.tripplan.domain.plan.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PlanReq {
	@NotEmpty
	private String title;

	@NotEmpty
	private String subtitle;

	@NotEmpty
	private String transportation;

	@NotEmpty
	private List<PlaceCategoryNamesReq> category;

	private int people;

	@NotNull
	private LocalDate startDate; // 시작일 추가

	@NotNull
	private LocalDate endDate;   // 종료일 추가

	@Valid
	private List<DayPlanReq> days;

}

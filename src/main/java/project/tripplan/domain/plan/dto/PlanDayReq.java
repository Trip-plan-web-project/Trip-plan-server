package project.tripplan.domain.plan.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PlanDayReq {
	private int day;
	private int cost;

	@NotNull
	private LocalDate date;

	@Valid
	private List<PlanDayDetailReq> detail;

}

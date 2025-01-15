package project.tripplan.domain.plan.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.tripplan.domain.plan.enums.PlanStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanStatusReq {
	@NotNull
	private PlanStatus status;
}

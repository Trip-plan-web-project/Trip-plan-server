package project.tripplan.domain.plan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import project.tripplan.domain.plan.entity.Plan;

@Getter
@Setter
@AllArgsConstructor
public class PlanContentAndTotalCountRes {

	private List<Plan> content;
	private long totalCount;
}

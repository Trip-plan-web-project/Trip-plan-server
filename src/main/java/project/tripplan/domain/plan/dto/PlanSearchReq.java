package project.tripplan.domain.plan.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanSearchReq extends PageRequestReq {

	private List<String> categoryNames;
	private Integer day;
	private String transportCategoryName;
	private Integer people;
}

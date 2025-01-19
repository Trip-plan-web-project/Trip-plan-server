package project.tripplan.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryNameDepthReq {
	private String name;
	private int depth;
}

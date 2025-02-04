package project.tripplan.domain.plan.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanConditionReq {
	private String title;
	private Integer size;
	@NotNull
	private String sortBy;
	private String direction;
	@NotEmpty
	private String lastValue;
	@NotNull
	private Long lastId;
	private Integer day;
	private String transportCategoryName;
	private Integer people;
	private List<String> categoryNames;

	public List<CategoryNameDepthReq> toCategoryReqList() {
		if (categoryNames == null || categoryNames.isEmpty()) {
			return Collections.emptyList();
		}
		List<CategoryNameDepthReq> result = new ArrayList<>();
		for (String catParam : categoryNames) {
			String[] tokens = catParam.split("-");
			if (tokens.length == 2) {
				String name = tokens[0];
				int depth = Integer.parseInt(tokens[1]);
				result.add(new CategoryNameDepthReq(name, depth));
			}
		}
		return result;
	}
}

package project.tripplan.domain.plan.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeRes {
	private List<PlanInfo> mostViewPlans;      // 조회수 높은 플랜 리스트
	private List<PlanInfo> mostRecentPlans;   // 최신 플랜 리스트
	private List<PlanInfo> hotPlacePlans;   // 홍대 인기 플랜 리스트

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PlanInfo {
		private Long planId;
		private String title;
		private List<String> placeCategory;
		private LocalDate startDate;
		private LocalDate endDate;
		private Integer people;
		private String transportation;
		private Integer totalCost;
		private String thumbnail;
	}
}

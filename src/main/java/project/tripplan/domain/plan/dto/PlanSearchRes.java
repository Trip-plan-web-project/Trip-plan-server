package project.tripplan.domain.plan.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import project.tripplan.domain.plan.entity.Plan;

@Getter
@Setter
public class PlanSearchRes {
	private Long planId;
	private String title;
	private String image;
	private List<String> category;
	private LocalDate startDate;
	private LocalDate endDate;
	private int people;
	private String transportCategoryName;
	private Long totalCost;

	public PlanSearchRes(Plan plan) {
		this.planId = plan.getId();
		this.title = plan.getTitle();
		this.image = plan.getImageUrl();
		this.startDate = plan.getStartDate();
		this.endDate = plan.getEndDate();
		this.people = plan.getPeople();
		this.totalCost = plan.getTotalCost();
	}

}

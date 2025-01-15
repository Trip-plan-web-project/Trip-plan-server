package project.tripplan.domain.plan.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.tripplan.domain.category.transportationCategory.enums.TransportationName;
import project.tripplan.domain.plan.enums.PlanStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanDetailRes {
	private String title;
	List<String> placeCategory = new ArrayList<>();
	private String author;
	private String profileImage;
	private LocalDateTime createdAt;
	private LocalDate startDate;
	private LocalDate endDate;
	private PlanStatus status;
	private Long viewCount;
	private Long like;
	private int people;
	private TransportationName transportation;
	private Long totalCost;
}

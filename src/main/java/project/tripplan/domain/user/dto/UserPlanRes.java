package project.tripplan.domain.user.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserPlanRes {

	private Long planId;
	private String title;
	private LocalDateTime createdAt;
	private String imageUrl;
	private List<String> categories;
	private String status;

	// Projections.constructor() 생성자
	public UserPlanRes(Long planId,
		String title,
		LocalDateTime createdAt,
		String imageUrl,
		String categoryNames,
		String status) {
		this.planId = planId;
		this.title = title;
		this.createdAt = createdAt;
		this.imageUrl = imageUrl;
		this.categories = (categoryNames == null || categoryNames.isEmpty())
			? List.of()
			: List.of(categoryNames.split(","));
		this.status = status;
	}
}

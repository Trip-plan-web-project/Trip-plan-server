package project.tripplan.domain.review.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddReviewReq {

	@NotEmpty
	private String title;

	@NotNull
	private Long placeId;

	@NotNull
	private double latitude;
	@NotNull
	private double longitude;

	@NotEmpty
	private String content;

	@NotNull
	private LocalDate visitedDay;
}

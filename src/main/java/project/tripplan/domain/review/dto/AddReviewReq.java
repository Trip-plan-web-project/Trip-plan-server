package project.tripplan.domain.review.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddReviewReq {

	@NotEmpty
	private String title;

	@NotNull
	private String placeId;

	@NotNull
	private double latitude;
	@NotNull
	private double longitude;

	@NotEmpty
	private String content;

	@NotNull
	private LocalDate visitedDay;

	@NotNull
	@Min(1)
	@Max(5)
	private Long averageRating;
}

package project.tripplan.domain.user.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserReviewRes {
	private Long reviewId;
	private String title;
	private Integer imageCount;
	private String contentText;
	private String contentImageUrl;
	private LocalDate createdAt;
}

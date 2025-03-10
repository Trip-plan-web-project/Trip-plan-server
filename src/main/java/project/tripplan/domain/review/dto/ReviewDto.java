package project.tripplan.domain.review.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDto {
	private Long reviewId;
	private String title;
	private LocalDate createdAt;
	private String userImageUrl;
	private String nickname;
	private String contentText;
	private String contentImageUrl;
	private int imageCount;
}

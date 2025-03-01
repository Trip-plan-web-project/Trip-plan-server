package project.tripplan.domain.review.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRes {

	private Long id;

	private String title;

	private String nickname;

	private String userImage;

	private String content;

	private Long viewCount;

	private Double latitude;

	private Double longitude;

	private LocalDate visitedDay;

}

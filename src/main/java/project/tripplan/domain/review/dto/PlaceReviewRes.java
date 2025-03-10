package project.tripplan.domain.review.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceReviewRes {
	private Long totalReviewCount; // 전체 리뷰 건수
	private List<ReviewDto> reviewSummaries;
}


package project.tripplan.domain.admin.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReportedReviewRes {
	private Long reviewId;
	private Long reviewReportId;
	private String reporter;
	private String reported;
	private String reviewTitle;
	private String reportCategory;
	private LocalDateTime createdAt;
	private List<Long> reportReason;

	public ReportedReviewRes(Long reviewId, Long reviewReportId, String reporter, String reported,
		String reviewTitle, String reportCategory, LocalDateTime createdAt, String reportReason) {
		this.reviewId = reviewId;
		this.reviewReportId = reviewReportId;
		this.reporter = reporter;
		this.reported = reported;
		this.reviewTitle = reviewTitle;
		this.reportCategory = reportCategory;
		this.createdAt = createdAt;
		this.reportReason = reportReason != null ? Arrays.stream(reportReason.split(","))
			.map(Long::parseLong)
			.collect(Collectors.toList()) : new ArrayList<>();
	}
}

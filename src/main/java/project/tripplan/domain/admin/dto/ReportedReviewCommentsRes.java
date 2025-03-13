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
public class ReportedReviewCommentsRes {
	private Long reviewCommentId;
	private Long reviewId;
	private Long reviewCommentReportId;
	private String reporter;
	private String reported;
	private String commentContent;
	private String reportCategory;
	private LocalDateTime createdAt;
	private List<Long> reportReason;

	public ReportedReviewCommentsRes(Long reviewCommentId, Long reviewId, Long reviewCommentReportId, String reporter,
		String reported,
		String commentContent, String reportCategory, LocalDateTime createdAt, String reportReason) {
		this.reviewCommentId = reviewCommentId;
		this.reviewId = reviewId;
		this.reviewCommentReportId = reviewCommentReportId;
		this.reporter = reporter;
		this.reported = reported;
		this.commentContent = commentContent;
		this.reportCategory = reportCategory;
		this.createdAt = createdAt;
		this.reportReason = reportReason != null ? Arrays.stream(reportReason.split(","))
			.map(Long::parseLong)
			.collect(Collectors.toList()) : new ArrayList<>();
	}
}

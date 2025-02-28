package project.tripplan.domain.admin.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReportedPlanListRes {
	private Long planId;
	private Long planReportId;
	private String reporter;
	private String reported;
	private String planTitle;
	private String reportCategory;
	private LocalDateTime createdAt;
	private List<Long> reportReason;

	public ReportedPlanListRes(Long planId, Long planReportId, String reporter,
		String reported, String planTitle, String reportCategory, LocalDateTime createdAt, String reportReason) {
		this.planId = planId;
		this.planReportId = planReportId;
		this.reporter = reporter;
		this.reported = reported;
		this.planTitle = planTitle;
		this.reportCategory = reportCategory;
		this.createdAt = createdAt;
		this.reportReason = convertStringToLongList(reportReason);
	}

	private List<Long> convertStringToLongList(String reasonIds) {
		if (reasonIds == null || reasonIds.isEmpty()) {
			return new ArrayList<>();
		}
		return Arrays.stream(reasonIds.split(","))
			.map(Long::parseLong)
			.collect(Collectors.toList());
	}
}

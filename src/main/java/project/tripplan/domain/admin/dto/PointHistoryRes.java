package project.tripplan.domain.admin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.tripplan.domain.point.enums.PointStatus;
import project.tripplan.domain.point.enums.PointType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PointHistoryRes {
	private Long pointId;
	private String nickname;
	private PointType pointType;
	private Integer point;
	private Long detailPageId;
	private PointStatus status;
	private LocalDateTime createAt;
}

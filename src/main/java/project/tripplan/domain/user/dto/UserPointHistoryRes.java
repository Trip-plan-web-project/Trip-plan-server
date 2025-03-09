package project.tripplan.domain.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.tripplan.domain.point.enums.PointType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPointHistoryRes {
	private LocalDateTime rewardDate;
	private PointType rewardReason;
	private Integer point;
	private Long detailId;
}

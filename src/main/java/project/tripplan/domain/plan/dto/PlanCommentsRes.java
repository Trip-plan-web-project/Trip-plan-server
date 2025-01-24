package project.tripplan.domain.plan.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanCommentsRes {
	private String socialId;
	private Long commentId;
	private String nickname;
	private LocalDateTime createdAt;
	private String content;
}

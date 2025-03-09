package project.tripplan.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.tripplan.domain.user.enums.UserRole;

@Builder
@Getter
@AllArgsConstructor
public class UserProfileRes {
	private Long userId;
	private String nickname;
	private String image;
	private UserRole userRole;
	private Integer point;
}

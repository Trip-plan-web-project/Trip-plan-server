package project.tripplan.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserProfileRes {
	private Long userId;
	private String nickname;
	private String image;
}

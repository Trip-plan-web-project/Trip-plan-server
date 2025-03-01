package project.tripplan.domain.comment.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentsRes {

	private String socialId;
	private Long commentId;
	private String profileImage;
	private String nickname;
	private LocalDateTime createdAt;
	private String content;

}

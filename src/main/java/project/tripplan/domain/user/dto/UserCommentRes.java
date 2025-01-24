package project.tripplan.domain.user.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class UserCommentRes {

	private Long planId;
	private Long commentId;
	private String title;
	private List<String> categories;
	private LocalDateTime createdAt;
	private String comment;

	public UserCommentRes(
		Long planId,
		Long commentId,
		String title,
		List<String> categories,
		LocalDateTime createdAt,
		String comment
	) {
		this.planId = planId;
		this.commentId = commentId;
		this.title = title;
		this.categories = categories;
		this.createdAt = createdAt;
		this.comment = comment;
	}
}

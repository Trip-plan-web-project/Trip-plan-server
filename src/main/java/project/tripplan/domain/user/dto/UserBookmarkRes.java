package project.tripplan.domain.user.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class UserBookmarkRes {

	private Long bookmarkId;
	private Long planId;
	private String title;

	private String thumbnail;
	private List<String> categories;

	public UserBookmarkRes(
		Long bookmarkId,
		Long planId,
		String title,
		String thumbnail,
		String categoryNames) {
		this.bookmarkId = bookmarkId;
		this.planId = planId;
		this.title = title;
		this.thumbnail = thumbnail;
		this.categories = (categoryNames == null || categoryNames.isEmpty())
			? List.of()
			: List.of(categoryNames.split(","));
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}

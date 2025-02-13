package project.tripplan.domain.plan.dto;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanNoOffsetReq {

	private String keyword;
	private int size;                  // 페이지 크기
	private String sortBy;             // 정렬 기준 (예: "viewCount", "id")
	private String direction;          // 정렬 방향 (예: "ASC", "DESC")
	private String lastValue;          // 커서로 사용되는 기준 값 (예: viewCount)
	private Long lastId;               // 커서로 사용되는 PK 값

	private List<CategoryNameDepthReq> categoryNames;

	private Integer day;               // 총 며칠 여행인지
	private String transportCategoryName; // "CAR" or "PUBLIC_TRANSPORT"
	private Integer people;            // 인원 수

	// categoryNames로 넘어온 req에 대해 Service 단에서 세팅해서 Repository로 넘길 필드
	private Set<Long> categoryNamecategoryIds;

	// title로 넘어온 req에 대해 Service 단에서 세팅해서 Repository로 넘길 필드
	private Set<Long> titleCategoryIds;

	public void setCategoryNamecategoryIds(Set<Long> categoryNamecategoryIds) {
		this.categoryNamecategoryIds = categoryNamecategoryIds;
	}

	public void setTitleCategoryIds(Set<Long> titleCategoryIds) {
		this.titleCategoryIds = titleCategoryIds;
	}
}

package project.tripplan.domain.plan.dto;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanNoOffsetReq {

	private int size = 10;          // 한 번에 가져올 개수 (default 10)
	private String sortBy = "id"; // 정렬 기준 필드 ("createdAt", "viewCount", "id" 등)
	private String direction = "DESC";   // 정렬 방향 ("ASC" / "DESC")

	// 검색 조건
	private List<String> categoryNames;
	private Integer day;
	private String transportCategoryName;
	private Integer people;

	// ★ 무한 스크롤 커서 (직전 페이지의 마지막 레코드 정보)
	// 예: 마지막 레코드의 정렬 필드 값 (createdAt, viewCount 등)
	private String lastValue;  // String으로 받되, 변환이 필요할 수 있음 (예: DateTime이면 "2025-01-19T10:00:00")
	private Long lastId;       // 마지막 레코드의 PK

	private Set<Long> categoryIds; // Service에서 채움
}

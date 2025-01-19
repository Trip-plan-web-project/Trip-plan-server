package project.tripplan.domain.plan.dto;

import java.util.List;

import lombok.Data;

@Data
public class PlanNoOffsetRes {
	private List<PlanSearchRes> plans; // 실제 검색 결과
	private Boolean hasNext;           // 다음 페이지 존재 여부
	private String nextValue;          // 다음 페이지 커서 값
	private Long nextId;               // 다음 페이지 커서 pk
}

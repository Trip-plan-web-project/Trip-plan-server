package project.tripplan.domain.plan.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanNoOffsetRes {

	private List<PlanSearchRes> plans;  // 실제 데이터
	private boolean hasNext;            // 다음 페이지 존재 여부

	// 다음 페이지 요청 시 넘길 값
	private String nextValue;  // 마지막 레코드의 정렬 필드 값 (문자열 형태)
	private Long nextId;       // 마지막 레코드의 PK
}

package project.tripplan.domain.plan.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageRequestReq {

	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 10;
	private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.DESC;
	private static final String DEFAULT_SORT_BY = "id";

	private int page = DEFAULT_PAGE;
	private int size = DEFAULT_SIZE;
	private Sort.Direction direction = DEFAULT_DIRECTION;
	private String sortBy = DEFAULT_SORT_BY;

	public Pageable toPageable() {
		return PageRequest.of(page - 1, size, direction, sortBy);
	}
}
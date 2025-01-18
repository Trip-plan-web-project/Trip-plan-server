package project.tripplan.domain.category.placeCategory.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;
import project.tripplan.domain.category.placeCategory.repository.PlaceCategoryRepository;

@Service
@RequiredArgsConstructor
public class PlaceCategoryService {

	private final PlaceCategoryRepository placeCategoryRepository;

	/**
	 * 예: categoryNames = ["안산시","서울시"]
	 *    → DB에서 이름이 안산시 or 서울시 인 Category 찾기 (rootList)
	 *    → 각 root의 모든 자식(후손)까지 ID를 수집
	 */
	public Set<Long> findAllDescendantCategoryIds(List<String> categoryNames) {
		// 1) 이름 목록에 해당하는 카테고리(들) 조회
		//    (부모 or 어떤 depth이든 이름이 정확히 일치하는 목록)
		List<PlaceCategory> rootList = placeCategoryRepository.findByNameIn(categoryNames);

		// 2) 결과 ID를 담을 Set
		Set<Long> resultIds = new HashSet<>();

		// 3) 각 root에 대해 DFS/재귀로 자식까지 ID 수집
		for (PlaceCategory root : rootList) {
			collectAllChildren(root, resultIds);
		}

		return resultIds;
	}

	/**
	 * root부터 시작해 모든 하위 노드(자식, 손자, ...)의 ID를 재귀로 수집
	 */
	private void collectAllChildren(PlaceCategory current, Set<Long> result) {
		if (current == null)
			return;
		result.add(current.getId());
		// children 필드에 자식들이 있다고 가정
		for (PlaceCategory child : current.getChildren()) {
			collectAllChildren(child, result);
		}
	}
}

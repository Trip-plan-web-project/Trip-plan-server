package project.tripplan.domain.category.placeCategory.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;
import project.tripplan.domain.category.placeCategory.repository.PlaceCategoryRepository;
import project.tripplan.domain.plan.dto.CategoryNameDepthReq;
import project.tripplan.domain.plan.dto.PlaceCategoryNamesReq;

@RequiredArgsConstructor
@Service
@Transactional
public class PlaceCategoryService {

	private final PlaceCategoryRepository placeCategoryRepository;

	public PlaceCategory searchPlaceCategory(PlaceCategoryNamesReq req) {
		// 1) parent 카테고리
		PlaceCategory parentCategory = null;
		if (hasText(req.getParent())) {
			parentCategory = placeCategoryRepository.findByNameAndParent(req.getParent(), null);

			// parentCategory가 없는 경우 생성
			if (parentCategory == null) {
				parentCategory = PlaceCategory.builder()
					.name(req.getParent())
					.depth(0)
					.parent(null)
					.build();
				placeCategoryRepository.save(parentCategory);
			}
		}

		// 2) child 카테고리
		if (hasText(req.getChild()) && parentCategory != null) {
			PlaceCategory childCategory = placeCategoryRepository.findByNameAndParent(req.getChild(), parentCategory);

			if (childCategory == null) {
				childCategory = PlaceCategory.builder()
					.name(req.getChild())
					.depth(parentCategory.getDepth() + 1)
					.parent(parentCategory)
					.build();
				placeCategoryRepository.save(childCategory);
			}
			// 자식이 있으면 자식을 최종 반환
			return childCategory;
		}

		// 자식이 없으면 parentCategory 반환
		return parentCategory;
	}

	private boolean hasText(String str) {
		return str != null && !str.trim().isEmpty();
	}

	/**
	 * 요청에 있는 여러 categoryNames(depth 포함)를 순회하면서
	 * - depth=0 => 해당 카테고리 + 모든 자손 ID
	 * - depth>0 => 해당 카테고리 자신만
	 * 결과를 하나의 Set에 담는다 => OR 조건
	 */
	public Set<Long> findAllDescendantCategoryIds(List<CategoryNameDepthReq> categoryNameDepthList) {
		Set<Long> result = new HashSet<>();
		if (categoryNameDepthList == null || categoryNameDepthList.isEmpty()) {
			return result;
		}

		for (CategoryNameDepthReq req : categoryNameDepthList) {
			// name, depth 로 카테고리 찾기 (직접 parent=null 조건 추가 등은 필요시 적용)
			PlaceCategory category = placeCategoryRepository.findByNameAndDepth(req.getName(), req.getDepth());
			if (category != null) {
				if (req.getDepth() == 0) {
					// depth=0 => 자손까지 포함
					result.addAll(getDescendantIds(category));
				} else {
					// depth>=1 => 자기 자신만
					result.add(category.getId());
				}
			}
		}
		return result;
		// -> 중복이면 Set으로 합쳐지고, 최종적으로 in(...) 연산 시 OR
	}

	/**
	 * 특정 카테고리(category) + 모든 하위 자손들의 id까지 재귀적으로 수집
	 */
	private Set<Long> getDescendantIds(PlaceCategory category) {
		Set<Long> ids = new HashSet<>();
		ids.add(category.getId());

		// 자식 목록 조회
		List<PlaceCategory> children = placeCategoryRepository.findByParent(category);
		for (PlaceCategory child : children) {
			ids.addAll(getDescendantIds(child));
		}

		return ids;
	}
}
package project.tripplan.domain.category.placeCategory.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;
import project.tripplan.domain.category.placeCategory.repository.PlaceCategoryRepository;
import project.tripplan.domain.category.placeCategory.repository.PlaceCategoryRepositoryCustom;
import project.tripplan.domain.plan.dto.CategoryNameDepthReq;
import project.tripplan.domain.plan.dto.PlaceCategoryNamesReq;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class PlaceCategoryService {

	private final PlaceCategoryRepository placeCategoryRepository;
	private final PlaceCategoryRepositoryCustom placeCategoryRepositoryCustom;

	public PlaceCategory searchPlaceCategory(PlaceCategoryNamesReq req) {
		// 1) parent 카테고리
		PlaceCategory parentCategory = null;
		PlaceCategory childCategory = null;
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
			childCategory = placeCategoryRepository.findByNameAndParent(req.getChild(), parentCategory);

			if (childCategory == null) {
				childCategory = PlaceCategory.builder()
					.name(req.getChild())
					.depth(parentCategory.getDepth() + 1)
					.parent(parentCategory)
					.build();
				placeCategoryRepository.save(childCategory);
			}
			// grandChild가 없거나 child 자체가 없을 때는 child 반환
			if (!hasText(req.getGrandChild())) {
				return childCategory;
			}
		}

		// 3) grandChild 카테고리
		if (hasText(req.getGrandChild()) && childCategory != null) {

			PlaceCategory grandChildCategory = placeCategoryRepository.findByNameAndParent(req.getGrandChild(),
				childCategory);
			if (grandChildCategory == null) {
				grandChildCategory = PlaceCategory.builder()
					.name(req.getGrandChild())
					.depth(childCategory.getDepth() + 1)
					.parent(childCategory)
					.build();
				placeCategoryRepository.save(grandChildCategory);
			}

			return grandChildCategory;
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
	 * - depth>1 => 해당 카테고리 + 모든 자손 ID
	 * - depth>2 => 해당 카테고리
	 * 결과를 하나의 Set에 담는다 => OR 조건
	 */
	public Set<Long> findAllDescendantCategoryIds(List<CategoryNameDepthReq> categoryNameDepthList) {
		// 요청이 비어 있으면 빈 Set 반환
		if (categoryNameDepthList == null || categoryNameDepthList.isEmpty()) {
			return Collections.emptySet();
		}

		// 최종 결과를 담을 Set
		return categoryNameDepthList.stream()
			// 요청의 (name, depth)로 카테고리 찾기
			.map(req -> placeCategoryRepository.findByNameAndDepth(req.getName(), req.getDepth()))
			// null(못 찾은 경우) 제외
			.filter(Objects::nonNull)
			// depth=0, 1이면 자손 포함, 그 외는 자기 자신만
			.flatMap(category -> {
				if (category.getDepth() == 0 || category.getDepth() == 1) {
					// 자손 포함 IDs
					return getDescendantIds(category).stream();
				} else if (category.getDepth() == 2) {
					// 자기 자신 ID만
					return Stream.of(category.getId());
				} else {
					//그 외 depth에 대한 처리
					throw new CustomException(BaseResponseCode.POST_PLAN_PLACE_FAIL);
				}
			})
			// 최종적으로 Set에 수집
			.collect(Collectors.toSet());
	}

	/**
	 * 특정 카테고리(category) + 모든 하위 자손들의 id까지 재귀적으로 수집
	 */
	private Set<Long> getDescendantIds(PlaceCategory root) {
		Set<Long> ids = new HashSet<>();
		Queue<PlaceCategory> queue = new LinkedList<>();
		queue.offer(root);

		while (!queue.isEmpty()) {
			PlaceCategory current = queue.poll();
			ids.add(current.getId());

			// 현재 카테고리의 모든 자식 조회
			List<PlaceCategory> children = placeCategoryRepository.findByParent(current);
			for (PlaceCategory child : children) {
				queue.offer(child);
			}
		}

		return ids;
	}

	public Set<Long> findAllSearchDescendantCategoryIds(String keyword) {
		Set<Long> resultIds = new HashSet<>();

		// 1) 전처리(접미사 제거 등) - 필요 없다면 생략 가능
		String normalized = normalizePlaceName(keyword);
		if (normalized.isEmpty()) {
			return resultIds; // 빈 세트 반환
		}

		// 2) DB 검색
		List<PlaceCategory> matchedCategories =
			placeCategoryRepositoryCustom.findByNameOrSynonymsContaining(normalized);
		if (matchedCategories.isEmpty()) {
			return resultIds;
		}

		// 3) 각각 자식 카테고리까지 포함하여 ID 추출
		for (PlaceCategory cat : matchedCategories) {
			resultIds.add(cat.getId());              // 본인 ID
			resultIds.addAll(getSearchDescendantIds(cat)); // 자식들 재귀적으로 탐색
		}

		return resultIds;
	}

	private Set<Long> getSearchDescendantIds(PlaceCategory parent) {
		Set<Long> ids = new HashSet<>();
		for (PlaceCategory child : parent.getChildren()) {
			ids.add(child.getId());
			ids.addAll(getDescendantIds(child));
		}
		return ids;
	}

	private String normalizePlaceName(String raw) {
		if (raw == null)
			return "";
		return raw.trim().replaceAll("(동|읍|면|시|구)$", "").trim();
	}
}
package project.tripplan.domain.category.placeCategory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;
import project.tripplan.domain.category.placeCategory.repository.PlaceCategoryRepository;
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
}

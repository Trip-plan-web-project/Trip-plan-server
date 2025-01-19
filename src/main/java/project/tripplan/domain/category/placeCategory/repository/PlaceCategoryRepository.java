package project.tripplan.domain.category.placeCategory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
	PlaceCategory findByNameAndParent(String name, PlaceCategory parent);

	// parent 하위의 자식 목록
	List<PlaceCategory> findByParent(PlaceCategory parent);

	// name + depth로 단일 카테고리 찾기
	PlaceCategory findByNameAndDepth(String name, int depth);
}

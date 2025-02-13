package project.tripplan.domain.category.placeCategory.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;

public interface PlaceCategoryRepositoryCustom {
	List<PlaceCategory> findByNameOrSynonymsContaining(@Param("keyword") String keyword);
}

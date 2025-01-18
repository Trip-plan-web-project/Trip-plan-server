package project.tripplan.domain.category.placeCategory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
	PlaceCategory findByNameAndParent(String name, PlaceCategory parent);

	List<PlaceCategory> findByNameIn(List<String> names);
}

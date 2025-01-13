package project.tripplan.domain.category.placeCategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {
        PlaceCategory findByNameAndParent(String name, PlaceCategory parent);
}

package project.tripplan.domain.category.transportationCategory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.category.transportationCategory.entitiy.TransportationCategory;
import project.tripplan.domain.category.transportationCategory.enums.TransportationName;

public interface TransportationCategoryRepository extends JpaRepository<TransportationCategory, Long> {
	Optional<TransportationCategory> findByName(TransportationName name);
}

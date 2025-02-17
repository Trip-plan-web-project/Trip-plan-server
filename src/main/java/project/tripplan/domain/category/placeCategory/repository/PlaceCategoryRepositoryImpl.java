package project.tripplan.domain.category.placeCategory.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.category.placeCategory.entity.PlaceCategory;
import project.tripplan.domain.category.placeCategory.entity.QPlaceCategory;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PlaceCategoryRepositoryImpl implements PlaceCategoryRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QPlaceCategory placeCategory = QPlaceCategory.placeCategory;

	public List<PlaceCategory> findByNameOrSynonymsContaining(String keyword) {

		return qf
			.selectFrom(placeCategory)
			.where(
				placeCategory.name.containsIgnoreCase(keyword)
			)
			.fetch();
	}

}

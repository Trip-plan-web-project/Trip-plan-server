package project.tripplan.domain.review.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.review.entity.QReviewImage;

@RequiredArgsConstructor
@Repository
public class ReviewImageRepositoryCustomImpl implements ReviewImageRepositoryCustom {
	private final JPAQueryFactory qf;
	private final QReviewImage reviewImage = QReviewImage.reviewImage;

	@Override
	public List<String> findAllImageUrls() {
		return qf.select(reviewImage.imageUrl)
			.from(reviewImage)
			.fetch();
	}
}

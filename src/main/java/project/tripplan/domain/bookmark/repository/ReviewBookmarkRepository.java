package project.tripplan.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.bookmark.entity.ReviewBookmark;

public interface ReviewBookmarkRepository extends JpaRepository<ReviewBookmark, Long> {
	
}

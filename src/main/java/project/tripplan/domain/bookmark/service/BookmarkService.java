package project.tripplan.domain.bookmark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.bookmark.entity.Bookmark;
import project.tripplan.domain.bookmark.repository.BookmarkRepository;
import project.tripplan.domain.bookmark.repository.BookmarkRepositoryCustom;
import project.tripplan.domain.plan.entity.Plan;
import project.tripplan.domain.plan.repository.PlanRepository;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final PlanRepository planRepository;
	private final BookmarkRepositoryCustom bookmarkRepositoryCustom;

	@Transactional
	public Long addBookmark(User user, Long planId) {
		Plan findPlan = planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.PLAN_NOT_EXIST));

		Bookmark bookmark = Bookmark.builder()
			.user(user)
			.plan(findPlan)
			.build();

		return bookmarkRepository.save(bookmark).getId();
	}

	@Transactional
	public void deleteBookmark(User user, Long bookmarkId) {
		Bookmark findBookmark = bookmarkRepositoryCustom.findByBookmarkIdWithUserId(bookmarkId, user.getId())
			.orElseThrow(() -> new CustomException(BaseResponseCode.BOOKMARK_NOT_EXIST));

		// 사용자가 다른 경우
		if(findBookmark.getUser().getId() != user.getId()) {
			throw new CustomException(BaseResponseCode.UNAUTHORIZED_BOOKMARK_DELETE);
		}

		bookmarkRepository.delete(findBookmark);
		return;
	}
}

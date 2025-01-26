package project.tripplan.domain.user.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import project.tripplan.domain.bookmark.repository.BookmarkRepositoryCustom;
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.comment.repository.CommentRepository;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanRepositoryCustom;
import project.tripplan.domain.user.dto.UserBookmarkRes;
import project.tripplan.domain.user.dto.UserCommentRes;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepository;
import project.tripplan.domain.user.repository.UserRepositoryCustom;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

	private final UserRepositoryCustom userRepositoryCustom;
	private final UserRepository userRepository;
	private final PlanRepositoryCustom planRepositoryCustom;
	private final BookmarkRepositoryCustom bookmarkRepositoryCustom;
	private final CommentRepository commentRepository;
	private final PlanPlaceCategoryRepositoryCustom planPlaceCategoryRepositoryCustom;

	private final S3Service s3Service;

	public void updateUserProfile(Long userId, String nickname, MultipartFile image) {
		User user = userRepository.findById(userId).get();
		if (nickname != null) {
			user.setNickname(nickname);
		}
		if (user.getImage() != null) {
			s3Service.deleteFile(user.getImage());
		}
		s3Service.uploadFile(image);
	}

	@Transactional(readOnly = true)
	public Page<UserPlanRes> getUserPlans(Long userId, Pageable pageable) {
		return planRepositoryCustom.findPlansByUserId(userId, pageable);
	}

	@Transactional(readOnly = true)
	public Page<UserBookmarkRes> getUserBookmarks(Long userId, Pageable pageable) {
		return bookmarkRepositoryCustom.findBookmarksByUserId(userId, pageable);
	}

	@Transactional(readOnly = true)
	public Page<UserCommentRes> getUserComments(Long userId, Pageable pageable) {
		Page<Comment> commentPage = commentRepository.findCommentsByUser(userId, pageable);

		// 1) 댓글에 연결된 planId 모으기 (중복제거)
		List<Long> planIds = commentPage
			.stream()
			.map(comment -> comment.getPlan().getId())
			.distinct()
			.toList();

		// 2) planIds에 해당하는 PlanPlaceCategory 전부 조회
		List<PlanPlaceCategory> planPlaceCategories = planPlaceCategoryRepositoryCustom.findAllByPlanIds(planIds);

		// 3) planId -> 카테고리명들
		Map<Long, List<String>> planCategoryMap = planPlaceCategories.stream()
			.collect(Collectors.groupingBy(
				ppc -> ppc.getPlan().getId(),
				Collectors.mapping(
					ppc -> ppc.getPlaceCategory().getName(),
					Collectors.toList()
				)
			));

		// 4) Page<Comment> -> Page<UserCommentRes> 변환
		List<UserCommentRes> dtoList = commentPage.getContent().stream()
			.map(c -> {
				Long planId = c.getPlan().getId();
				List<String> categories = planCategoryMap.getOrDefault(planId, List.of());

				return new UserCommentRes(
					planId,
					c.getId(),
					c.getPlan().getTitle(),
					categories,
					c.getCreatedAt(),
					c.getContent()
				);
			})
			.toList();

		// Page<UserCommentRes> 생성
		return new PageImpl<>(dtoList, pageable, commentPage.getTotalElements());
	}
}
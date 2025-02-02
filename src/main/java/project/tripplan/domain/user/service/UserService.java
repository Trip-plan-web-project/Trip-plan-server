package project.tripplan.domain.user.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.bookmark.repository.BookmarkRepositoryCustom;
import project.tripplan.domain.comment.entity.Comment;
import project.tripplan.domain.comment.repository.CommentRepositoryCustom;
import project.tripplan.domain.plan.entity.PlanPlaceCategory;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.plan.repository.PlanPlaceCategoryRepositoryCustom;
import project.tripplan.domain.plan.repository.PlanRepositoryCustom;
import project.tripplan.domain.user.dto.UserBookmarkRes;
import project.tripplan.domain.user.dto.UserCommentsRes;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.dto.UserProfileReq;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepository;
import project.tripplan.domain.user.repository.UserRepositoryCustom;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	@Value("${cloud.prefix}")
	private String prefix;

	private final UserRepositoryCustom userRepositoryCustom;
	private final UserRepository userRepository;
	private final PlanRepositoryCustom planRepositoryCustom;
	private final BookmarkRepositoryCustom bookmarkRepositoryCustom;
	private final CommentRepositoryCustom commentRepositoryCustom;
	private final PlanPlaceCategoryRepositoryCustom planPlaceCategoryRepositoryCustom;
	private final S3Service s3Service;

	public void updateUserProfile(Long userId, UserProfileReq req, MultipartFile image) {
		User user = userRepository.findById(userId).get();
		if (req.getNickname() != null) {
			user.setNickname(req.getNickname());
		}
		if (user.getImage() != null) {
			s3Service.deleteFile(user.getImage());
		}
		s3Service.uploadFile(image);
	}

	@Transactional(readOnly = true)
	public Page<UserPlanRes> getUserPlans(Long userId, Pageable pageable) {
		Page<UserPlanRes> findPlans = planRepositoryCustom.findPlansByUserId(userId, pageable);
		List<UserPlanRes> content = findPlans.getContent();

		for (UserPlanRes userPlan : content) {
			String thumb = userPlan.getThumbnail();
			if (thumb != null && !thumb.isBlank()) {
				userPlan.setThumbnail(prefix + "/" + thumb);
			}
		}
		return new PageImpl<>(content, pageable, findPlans.getTotalElements());
	}

	@Transactional(readOnly = true)
	public Page<UserBookmarkRes> getUserBookmarks(Long userId, Pageable pageable) {

		Page<UserBookmarkRes> findBookmarks = bookmarkRepositoryCustom.findBookmarksByUserId(userId, pageable);
		List<UserBookmarkRes> content = findBookmarks.getContent();

		for (UserBookmarkRes userBookmark : content) {
			String thumb = userBookmark.getThumbnail();
			if (thumb != null && !thumb.isBlank()) {
				userBookmark.setThumbnail(prefix + "/" + thumb);
			}
		}
		return new PageImpl<>(content, pageable, findBookmarks.getTotalElements());
	}

	public UserCommentsRes getUserCommentsNoOffset(Long userId, Long lastCommentId, int size) {
		long totalCount = commentRepositoryCustom.countByUserId(userId);

		List<Comment> commentList = commentRepositoryCustom.findCommentsByUserNoOffset(
			userId,
			lastCommentId,
			size + 1
		);

		boolean hasNext = false;
		if (commentList.size() > size) {
			hasNext = true;
			commentList.remove(commentList.size() - 1);
		}

		Long nextId = null;
		if (!commentList.isEmpty()) {
			nextId = commentList.get(commentList.size() - 1).getId();
		}

		List<Long> planIds = commentList.stream()
			.map(comment -> comment.getPlan().getId())
			.distinct()
			.toList();

		List<PlanPlaceCategory> planPlaceCategories =
			planPlaceCategoryRepositoryCustom.findAllByPlanIds(planIds);

		Map<Long, List<String>> planCategoryMap = planPlaceCategories.stream()
			.collect(Collectors.groupingBy(
				ppc -> ppc.getPlan().getId(),
				Collectors.mapping(
					ppc -> ppc.getPlaceCategory().getName(),
					Collectors.toList()
				)
			));

		List<UserCommentsRes.UserCommentsNoOffsetDto> commentResList = commentList.stream()
			.map(c -> {
				Long planId = c.getPlan().getId();
				List<String> categories = planCategoryMap.getOrDefault(planId, List.of());
				return new UserCommentsRes.UserCommentsNoOffsetDto(
					planId,
					c.getId(),
					c.getPlan().getTitle(),
					categories,
					c.getCreatedAt(),
					c.getContent()
				);
			})
			.toList();

		return new UserCommentsRes(
			commentResList,
			hasNext,
			nextId,
			totalCount
		);
	}
}


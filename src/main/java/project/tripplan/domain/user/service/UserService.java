package project.tripplan.domain.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import project.tripplan.domain.bookmark.repository.BookmarkRepositoryCustom;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.plan.repository.PlanRepositoryCustom;
import project.tripplan.domain.user.dto.UserBookmarkRes;
import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepository;
import project.tripplan.domain.user.repository.UserRepositoryCustom;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepositoryCustom userRepositoryCustom;
	private final UserRepository userRepository;
	private final PlanRepositoryCustom planRepositoryCustom;
	private final BookmarkRepositoryCustom bookmarkRepositoryCustom;

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
}
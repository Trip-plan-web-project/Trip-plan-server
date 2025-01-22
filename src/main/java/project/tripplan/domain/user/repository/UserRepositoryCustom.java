package project.tripplan.domain.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.tripplan.domain.user.dto.UserPlanRes;
import project.tripplan.domain.user.entity.User;

public interface UserRepositoryCustom {
	Optional<User> findBySocialId(String socialId);

	Page<UserPlanRes> findPlansByUserId(Long userId, Pageable pageable);
}

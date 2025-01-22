package project.tripplan.domain.user.repository;

import java.util.Optional;

import project.tripplan.domain.user.entity.User;

public interface UserRepositoryCustom {
	Optional<User> findBySocialId(String socialId);

}

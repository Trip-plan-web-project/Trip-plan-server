package project.tripplan.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.enums.Provider;

public interface UserRepositoryCustom {
	Optional<User> findBySocialId(String socialId);
}

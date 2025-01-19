package project.tripplan.domain.user.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import project.tripplan.domain.user.repository.UserRepositoryCustom;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepositoryCustom userRepositoryCustom;
}

package project.tripplan.domain.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.tripplan.domain.user.entity.User;
import project.tripplan.domain.user.repository.UserRepositoryCustom;
import project.tripplan.global.common.exception.CustomException;
import project.tripplan.global.common.response.BaseResponseCode;

@Service
@RequiredArgsConstructor
public class SocialLoginService implements UserDetailsService {
	private final UserRepositoryCustom userRepositoryCustom;

	@Override
	public UserDetails loadUserByUsername(String socialId) throws UsernameNotFoundException {
		User findUser = userRepositoryCustom.findBySocialId(socialId)
			.orElseThrow(() -> new CustomException(BaseResponseCode.USER_NOT_EXIST));

		return org.springframework.security.core.userdetails.User.builder()
			.username(findUser.getSocialId())
			.roles(findUser.getUserRole().name())
			.build();
	}
}

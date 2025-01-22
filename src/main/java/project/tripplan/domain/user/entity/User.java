package project.tripplan.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tripplan.domain.user.enums.Provider;
import project.tripplan.domain.user.enums.UserRole;
import project.tripplan.global.common.entity.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false)
	private String socialId;

	@Column
	private String email;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Column(columnDefinition = "TEXT")
	private String image;

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}

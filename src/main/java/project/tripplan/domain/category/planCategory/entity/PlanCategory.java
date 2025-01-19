package project.tripplan.domain.category.planCategory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.tripplan.global.common.entity.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class PlanCategory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_category_id")
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String image;

	@Column(unique = true, nullable = false)
	private String code;
}

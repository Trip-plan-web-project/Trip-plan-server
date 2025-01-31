package project.tripplan.domain.planDayDetail.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanDetailDayRes {

	private LocalDate date;
	private Integer cost;
	private List<PlaceDetail> places;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PlaceDetail {
		private Integer order;
		private String placeName;
		private String streetAddress;
		private String code;
		private Double latitude;
		private Double longitude;
	}
}

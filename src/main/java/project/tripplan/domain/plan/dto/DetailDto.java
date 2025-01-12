package project.tripplan.domain.plan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailDto {
    private int order;
    private String place;
    private String streetAddress;
    private double latitude;
    private double longitude;
    private String planCategoryName;
}

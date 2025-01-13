package project.tripplan.domain.plan.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DayPlanDto {
    private int day;
    private int cost;
    private LocalDate date;
    private List<DetailDto> detail;

}

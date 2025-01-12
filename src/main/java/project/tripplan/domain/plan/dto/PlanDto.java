package project.tripplan.domain.plan.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PlanDto {
    private String title;
    private String subtitle;
    private List<String> category;
    private int people;
    private LocalDate startDate; // 시작일 추가
    private LocalDate endDate;   // 종료일 추가
    private List<DayPlanDto> days;

}

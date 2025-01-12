package project.tripplan.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import project.tripplan.domain.plan.dto.PlanDto;
import project.tripplan.domain.plan.service.PlanService;
import project.tripplan.global.common.response.BaseResponse;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    @PostMapping("/plans/new")
    public BaseResponse<?> createPlan(
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("plan") PlanDto planDto
    ) throws IOException {

        planService.savePlan(planDto, thumbnail);

        return new BaseResponse<>("여행 계획하기 작성 성공", 201);
    }
}

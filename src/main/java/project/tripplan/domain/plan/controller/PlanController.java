package project.tripplan.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import project.tripplan.domain.plan.dto.PlanDto;
import project.tripplan.domain.plan.service.PlanService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    @PostMapping("/plans")
    public BaseResponse<?> createPlan(
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("plan") PlanDto planDto,
            @AuthenticationPrincipal User user
    ) throws IOException {
        return new BaseResponse<>(BaseResponseCode.ADD_PLAN_SUCCESS, planService.savePlan(user ,planDto, thumbnail));
    }
}

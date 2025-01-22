// package project.tripplan.domain.user.controller;
//
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import project.tripplan.domain.plan.dto.PlanDetailRes;
// import project.tripplan.domain.user.entity.User;
// import project.tripplan.global.common.response.BaseResponse;
// import project.tripplan.global.common.response.BaseResponseCode;
//
// @Slf4j
// @RestController
// @RequiredArgsConstructor
// public class UserController {
//
// 	@GetMapping
// 	public BaseResponse<PlanDetailRes> getUserProFile(@AuthenticationPrincipal User user) {
// 		return new BaseResponse<>(BaseResponseCode.GET_PLAN_DETAIL_INFO_SUCCESS,
// 			planService.getPlanInfoDetails(user, planId));
// 	}
// }

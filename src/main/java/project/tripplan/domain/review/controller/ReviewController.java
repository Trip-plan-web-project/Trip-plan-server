package project.tripplan.domain.review.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.tripplan.domain.plan.file.S3Service;
import project.tripplan.domain.review.dto.AddReviewReq;
import project.tripplan.domain.review.dto.ReviewImageRes;
import project.tripplan.domain.review.dto.ReviewRes;
import project.tripplan.domain.review.service.ReviewService;
import project.tripplan.domain.user.entity.User;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.response.BaseResponseCode;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

	@Value("${cloud.image-prefix}")
	private String imagePrefix;

	private final ReviewService reviewService;
	private final S3Service s3Service;

	@PostMapping("/review")
	public BaseResponse<Void> addReview(@AuthenticationPrincipal User user, @RequestBody AddReviewReq reviewReq) {
		reviewService.addReview(user, reviewReq);
		return new BaseResponse<>(BaseResponseCode.ADD_REVIEW_SUCCESS);
	}

	@GetMapping("/review/{reviewId}")
	public BaseResponse<ReviewRes> getReview(@AuthenticationPrincipal User user, @PathVariable Long reviewId) {
		return new BaseResponse<>(BaseResponseCode.GET_REVIEW_SUCCESS, reviewService.getReview(user, reviewId));
	}

	@PostMapping("/review/image/upload")
	public BaseResponse<ReviewImageRes> uploadReviewImage(
		@AuthenticationPrincipal User user,
		@RequestParam("image") MultipartFile image) {

		return new BaseResponse<>(BaseResponseCode.GET_REVIEW_SUCCESS,
			ReviewImageRes.builder()
				.imageUrl(
					imagePrefix + "/" + s3Service.uploadReviewFile(image))
				.build());
	}

	@DeleteMapping("/review/{reviewId}")
	public BaseResponse<Void> deleteReview(@AuthenticationPrincipal User user, @PathVariable Long reviewId) {
		reviewService.deleteReview(user, reviewId);
		return new BaseResponse<>(BaseResponseCode.DELETE_REVIEW_SUCCESS);
	}

}

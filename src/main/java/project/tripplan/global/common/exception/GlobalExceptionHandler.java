package project.tripplan.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import project.tripplan.global.common.response.BaseResponseCode;
import project.tripplan.global.common.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 보안할 점 : 404 ,405 Error 등 Dispatcher Servlet 이 잡아서 에러 던지는 거 처리해야 함.
	 */

	/**
	 * 커스텀 예외 처리
	 */
	@ExceptionHandler(CustomException.class)
	public BaseResponse<?> handleCustomException(CustomException e) {
		log.error("[handleCustomException] : {}", e.getMessage(), e);

		// CustomException에서 BaseResponseCode 가져오기
		BaseResponseCode responseCode = e.getBaseResponseCode();
		return new BaseResponse<>(
			responseCode,
			null // 데이터가 없으므로 null
		);
	}

	/**
	 * 그 외 모든 예외 처리
	 */
	@ExceptionHandler(Exception.class)
	public BaseResponse<?> handleException(Exception e) {
		log.error("[handleException] : {}", e.getMessage(), e);

		// 기본 서버 오류 처리
		BaseResponseCode responseCode = BaseResponseCode.INTERNAL_SERVER_ERROR;
		return new BaseResponse<>(
			responseCode,
			null // 데이터가 없으므로 null
		);
	}


	@ExceptionHandler(BindException.class)
	public BaseResponse<?> bindException(BindException e) {
		List<ErrorField> errorFields = new ArrayList<>();

		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			errorFields.add(new ErrorField(fieldError.getField(), fieldError.getDefaultMessage()));
		}

		BaseResponseCode responseCode = BaseResponseCode.VALIDATION_FAILED;

		return new BaseResponse<>(
				responseCode,
				errorFields
		);
	}

	@Getter
	@AllArgsConstructor
	public static class ErrorField {
		private Object value;
		private String message;
	}
}

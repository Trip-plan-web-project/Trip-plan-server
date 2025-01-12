package project.tripplan.global.common.interceptor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import project.tripplan.global.common.response.BaseResponse;

@Slf4j
@RestControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return BaseResponse.class.isAssignableFrom(returnType.getParameterType());
	}

	@Override
	public Object beforeBodyWrite(
		Object body,
		MethodParameter returnType,
		MediaType selectedContentType,
		Class selectedConverterType,
		ServerHttpRequest request,
		ServerHttpResponse response
	) {
		if (body instanceof BaseResponse<?> baseResponse) {
			int code = baseResponse.getCode();

			HttpStatus httpStatus;
			if (code >= 1000 && code < 2000) {
				// 1000번대: 성공
				httpStatus = HttpStatus.OK;
			} else if (code >= 2000 && code < 3000) {
				// 2000번대: request 오류
				httpStatus = HttpStatus.BAD_REQUEST;
			} else if (code >= 3000 && code < 4000) {
				// 3000번대: response 오류
				httpStatus = HttpStatus.UNPROCESSABLE_ENTITY; // HTTP 422
			} else if (code >= 4000) {
				// 4000번대: 서버 오류
				httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			} else {
				// 알 수 없는 상태 (기본값으로 처리)
				httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			}

			// HTTP 응답 상태 코드 설정
			response.setStatusCode(httpStatus);
		}
		return body;
	}
}


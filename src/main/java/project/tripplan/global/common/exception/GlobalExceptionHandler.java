package project.tripplan.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.tripplan.global.common.response.BaseResponseCode;
import project.tripplan.global.common.response.BaseResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 보안할 점 : 404 ,405 Error 등 Dispatcher Servlet 이 잡아서 에러 던지는거 처리 해야함.
     */

    /**
     * 커스텀 예외 처리
     */
    @ExceptionHandler(CustomException.class)
    public BaseResponse<?> handleCustomException(CustomException e) {
        log.error("[handleCustomException] : {}", e.getMessage(), e);

        BaseResponseCode ec = e.getBaseResponseCode();
        return new BaseResponse<>(
                ec.getStatus(),
                ec.getCode(),
                ec.getMessage(),
                ec.getHttpStatus()
        );
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleException(Exception e) {
        log.error("[handleException] : {}", e.getMessage(), e);

        BaseResponseCode ec = BaseResponseCode.INTERNAL_SERVER_ERROR;
        return new BaseResponse<>(
                ec.getStatus(),
                ec.getCode(),
                ec.getMessage(),
                ec.getHttpStatus()
        );
    }
}

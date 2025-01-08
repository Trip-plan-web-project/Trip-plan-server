package project.tripplan.global.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseResponse<T> {

    // 성공(true) / 실패(false)
    private final boolean status;

    private final Integer code;

    private final String message;

    /**
     * HTTP 응답코드 (JSON 응답에는 넣지 않음)
     * Response HttpStatus에 넣기 위해 존재.
     * 객체를 반환하기전 @ResponseBody (ResponseInterceptor -> @RestControllerAdvice)가 데이터 가공 후 JSON 반환함.
     */
    @JsonIgnore
    private final HttpStatus httpStatus;

    // 성공 시 반환할 데이터 (null 일 경우 JSON에서 제외)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    /**
     * 실패(에러) 시 사용
     */
    public BaseResponse(boolean status, Integer code, String message, HttpStatus httpStatus) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = null;
    }

    /**
     * 성공(데이터 있음)
     */
    public BaseResponse(String message, T data, Integer code) {
        this.status = true;
        this.code = code;
        this.message = message;
        this.data = data;
        this.httpStatus = HttpStatus.OK;
    }

    /**
     * 성공(데이터 없음)
     */
    public BaseResponse(String message,  Integer code) {
        this.status = true;
        this.code = code;
        this.message = message;
        this.data = null;
        this.httpStatus = HttpStatus.OK;
    }
}

package project.tripplan.global.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class BaseResponse<T> {

	private final boolean status;
	private final Integer code;
	private final String message;
	// 성공 시 반환할 데이터 (null 일 경우 JSON에서 제외)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public BaseResponse(BaseResponseCode responseCode) {
		this.status = responseCode.getStatus();
		this.message = responseCode.getMessage();
		this.code = responseCode.getCode();
	}

	public BaseResponse(BaseResponseCode responseCode, T data) {
		this.status = responseCode.getStatus();
		this.message = responseCode.getMessage();
		this.code = responseCode.getCode();
		this.data = data;
	}
}

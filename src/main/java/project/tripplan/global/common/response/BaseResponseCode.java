package project.tripplan.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseResponseCode {
    /**
     * 2000 : request 오류
     */

    //user
    USER_NOT_EXIST(false, 2001, "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),

    /**
     * 4000 : server 오류
     */
    INTERNAL_SERVER_ERROR(false,4001,"서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

   private final Boolean status;
    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}

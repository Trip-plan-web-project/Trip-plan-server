package project.tripplan.global;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.tripplan.global.code.ErrorCode;
import project.tripplan.global.common.response.BaseResponse;
import project.tripplan.global.common.exception.CustomException;

@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * 성공(데이터 있음)
     */
    @GetMapping("/hello")
    public BaseResponse<String> hello() {
        return new BaseResponse<>("여행 계획하기 작성 성공", "Hello, World!", HttpStatus.OK.value());
    }

    /**
     * 성공(데이터 없음)
     */
    @GetMapping("/null-data")
    public BaseResponse<Void> nullDataTest() {
        return new BaseResponse<>("성공했으나 data는 없음", HttpStatus.OK.value());
    }


    //----------------- Custom ErrorCode Test -----------------//

    /**
     * 1) TEST_ERROR(100, BAD_REQUEST)
     */
    @GetMapping("/test-error")
    public BaseResponse<Void> testError() {
        throw new CustomException(ErrorCode.TEST_ERROR);
    }

    /**
     * 2) NOT_FOUND_END_POINT(404, NOT_FOUND)
     */
    @GetMapping("/not-found-endpoint")
    public BaseResponse<Void> notFoundEndpoint() {
        throw new CustomException(ErrorCode.NOT_FOUND_END_POINT);
    }

    /**
     * 3) INTERNAL_SERVER_ERROR(500, INTERNAL_SERVER_ERROR)
     */
    @GetMapping("/internal-server-error")
    public BaseResponse<Void> internalServerError() {
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 4) ERROR_WHILE_PARSING_JSON(500, INTERNAL_SERVER_ERROR)
     */
    @GetMapping("/error-while-parsing-json")
    public BaseResponse<Void> errorWhileParsingJson() {
        throw new CustomException(ErrorCode.ERROR_WHILE_PARSING_JSON);
    }
}

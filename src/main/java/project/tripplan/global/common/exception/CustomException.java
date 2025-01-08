package project.tripplan.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.tripplan.global.common.response.BaseResponseCode;


@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final BaseResponseCode baseResponseCode;

    @Override
    public String getMessage() {
        return baseResponseCode.getMessage();
    }
}


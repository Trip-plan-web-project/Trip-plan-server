package project.tripplan.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.tripplan.global.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}


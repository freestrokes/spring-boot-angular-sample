package com.registry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.registry.exception.GlobalErrorCodes.BAD_REQUEST_CODE;


/**
 * 접근 거부 오류시 발생 (Role처리 오류)
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class BadRequestException extends BaseException {

    BindingResult bindingResult;

    public BadRequestException(String message) {
        super(BAD_REQUEST_CODE, message);
    }

    public BadRequestException(BindingResult bindingResult) {
        super(BAD_REQUEST_CODE, bindingResult.toString());
        this.bindingResult = bindingResult;
    }

    public BadRequestException(Throwable cause) {
        super(BAD_REQUEST_CODE, cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(BAD_REQUEST_CODE, message, cause);
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}

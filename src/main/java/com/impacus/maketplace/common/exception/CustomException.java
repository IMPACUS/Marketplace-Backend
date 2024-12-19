package com.impacus.maketplace.common.exception;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final Object detail;
    private final ErrorType errorType;

    public CustomException(HttpStatus status, ErrorType errorType) {
        this.status = status;
        this.detail = "";
        this.errorType = errorType;
    }

    public CustomException(HttpStatus status, ErrorType errorType, Object detail) {
        this.status = status;
        this.detail = detail;
        this.errorType = errorType;
    }

    public CustomException(ErrorType errorType) {
        this.status = HttpStatus.BAD_REQUEST;
        this.detail = "";
        this.errorType = errorType;
    }

    public CustomException(ErrorType errorType, Object detail) {
        this.status = HttpStatus.BAD_REQUEST;
        this.detail = detail;
        this.errorType = errorType;
    }


    public CustomException(ErrorType errorType, Throwable cause) {
        this.status = HttpStatus.BAD_REQUEST;
        this.detail = cause.getMessage();
        this.errorType = errorType;
    }

    public CustomException(Exception exception) {
        if (exception.getClass() == CustomException.class) {
            CustomException customException = (CustomException) exception;
            this.status = customException.getStatus();
            this.detail = customException.getDetail();
            this.errorType = customException.errorType;
        } else {
            this.status = HttpStatus.BAD_REQUEST;
            this.detail = exception.getMessage();
            this.errorType = CommonErrorType.UNKNOWN;
        }
    }

    public CustomException(HttpStatus httpStatus, Exception exception) {
        this.status = httpStatus;
        if (exception.getClass() == CustomException.class) {
            CustomException customException = (CustomException) exception;
            this.detail = customException.getDetail();
            this.errorType = customException.errorType;
        } else {
            this.detail = exception.getMessage();
            this.errorType = CommonErrorType.UNKNOWN;
        }
    }

}

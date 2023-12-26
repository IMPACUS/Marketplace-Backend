package com.impacus.maketplace.common.exception;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorType errorType;
    private final String detail;

    public CustomException(HttpStatus status, ErrorType errorType) {
        this.status = status;
        this.errorType = errorType;
        this.detail = "";
    }

    public CustomException(HttpStatus status, ErrorType errorType, String detail) {
        this.status = status;
        this.errorType = errorType;
        this.detail = detail;
    }

    public CustomException(ErrorType errorType) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorType = errorType;
        this.detail = "";
    }

    public CustomException(ErrorType errorType, String detail) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorType = errorType;
        this.detail = detail;
    }


    public CustomException(ErrorType errorType, Throwable cause) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorType = errorType;
        this.detail = cause.getMessage();
    }

    public CustomException(Exception exception) {
        if (exception.getClass() == CustomException.class) {
            CustomException customException = (CustomException) exception;
            this.status = customException.getStatus();
            this.errorType = customException.getErrorType();
            this.detail = customException.getDetail();
        } else {
            this.status = HttpStatus.BAD_REQUEST;
            this.errorType = ErrorType.UNKNOWN;
            this.detail = exception.getMessage();
        }
    }

}

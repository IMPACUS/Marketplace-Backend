package com.impacus.maketplace.common.exception;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;
    private final String errorMsg;
    private final Object detail;

    public CustomException(HttpStatus status, ErrorType errorType) {
        this.status = status;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = "";
    }

    public CustomException(HttpStatus status, ErrorType errorType, Object detail) {
        this.status = status;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = detail;
    }

    public CustomException(ErrorType errorType) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = "";
    }

    public CustomException(ErrorType errorType, Object detail) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = detail;
    }


    public CustomException(ErrorType errorType, Throwable cause) {
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMsg();
        this.detail = cause.getMessage();
    }

    public CustomException(Exception exception) {
        if (exception.getClass() == CustomException.class) {
            CustomException customException = (CustomException) exception;
            this.status = customException.getStatus();
            this.errorCode = customException.errorCode;
            this.errorMsg = customException.errorMsg;
            this.detail = customException.getDetail();
        } else {
            this.status = HttpStatus.BAD_REQUEST;
            this.errorCode = CommonErrorType.UNKNOWN.getCode();
            this.errorMsg = "";
            this.detail = exception.getMessage();
        }
    }

}

package com.impacus.maketplace.common.exception;

import com.impacus.maketplace.common.enumType.ErrorType;
import lombok.Getter;

@Getter
public class Custom400Exception extends RuntimeException {

    private final ErrorType errorType;
    private final String detail;

    public Custom400Exception(ErrorType errorType) {
        this.errorType = errorType;
        this.detail = "";
    }

    public Custom400Exception(ErrorType errorType, Throwable cause) {
        this.errorType = errorType;
        this.detail = cause.getMessage();
    }

    public Custom400Exception(Custom400Exception custom400Exception) {
        this.errorType = custom400Exception.getErrorType();
        this.detail = custom400Exception.getDetail();
    }

    public Custom400Exception(Throwable cause) {
        this.errorType = ErrorType.UNKNOWN;
        this.detail = cause.getMessage();
    }

}

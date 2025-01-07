package com.impacus.maketplace.common.utils;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponseEntity<T> extends ResponseEntity<T> {

    private boolean result;

    private HttpStatus code = HttpStatus.OK;

    private String message;

    private T data;

    public ApiResponseEntity() {
        super(HttpStatus.OK);
    }

    @Builder
    public ApiResponseEntity(
            HttpStatus code,
            T data,
            Boolean result,
            String message
    ) {
        super(data, code);
        this.data = data;
        this.code = code;
        this.result = result == null || result;
        this.message = message;
    }

    public static ApiResponseEntity<Boolean> simpleResult(Object t, HttpStatus failHttpStatus) {
        return ApiResponseEntity.<Boolean>builder()
                .result(t != null ? true : false)
                .data(t != null ? true : false)
                .code(t != null ? HttpStatus.OK : failHttpStatus)
                .build();
    }

    public static ApiResponseEntity<Boolean> simpleResult(HttpStatus httpStatus) {
        return ApiResponseEntity.<Boolean>builder()
                .result(true)
                .data(true)
                .code(httpStatus)
                .build();
    }

    public static <T> ApiResponseEntity<T> of(T data) {
        return ApiResponseEntity.<T>builder()
                .data(data)
                .build();
    }

}

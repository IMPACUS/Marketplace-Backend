package com.impacus.maketplace.common.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseEntity<T> {

    @Builder.Default
    private boolean result = true;

    @Builder.Default
    private boolean isAuthError = false;

    @Builder.Default
    private HttpStatus code = HttpStatus.OK;

    private String message;

    private T data;

    public static ApiResponseEntity<Boolean> simpleResult(Object t, HttpStatus failHttpStatus) {
        return ApiResponseEntity.<Boolean>builder()
                .result(t != null ? true : false)
                .data(t != null ? true : false)
                .code(t != null ? HttpStatus.OK : failHttpStatus)
                .build();
    }
}

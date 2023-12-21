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
    private HttpStatus code;
    private String message;

    private T data;
}

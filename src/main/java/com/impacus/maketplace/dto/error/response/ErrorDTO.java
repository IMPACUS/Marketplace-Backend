package com.impacus.maketplace.dto.error.response;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorDTO(String code, String msg, String detail) {
    public static ResponseEntity<Object> toResponseEntity(CustomException ex) {
        ErrorType errorType = ex.getErrorType();
        String detail = ex.getDetail();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDTO.builder()
                        .code(errorType.getCode())
                        .msg(errorType.getMsg())
                        .detail(detail)
                        .build());
    }
}

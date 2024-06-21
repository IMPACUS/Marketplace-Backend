package com.impacus.maketplace.dto.error.response;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorDTO(String code, String msg, Object detail) {
    public static ResponseEntity<Object> toResponseEntity(CustomException ex) {
        ErrorType errorType = ex.getErrorType();
        Object detail = ex.getDetail();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDTO.toDTO(errorType, detail));
    }

    public static ErrorDTO toDTO(ErrorType errorType) {
        return ErrorDTO.builder()
                .code(errorType.getCode())
                .msg(errorType.getMsg())
                .build();
    }

    public static ErrorDTO toDTO(ErrorType errorType, Object detail) {
        return ErrorDTO.builder()
                .code(errorType.getCode())
                .msg(errorType.getMsg())
                .detail(detail)
                .build();
    }
}

package com.impacus.maketplace.dto.error.response;

import com.impacus.maketplace.common.exception.CustomException;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorDTO(String code, String msg, Object detail) {
    public static ResponseEntity<Object> toResponseEntity(CustomException ex) {
        Object detail = ex.getDetail();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDTO.builder()
                        .code(ex.getErrorCode())
                        .msg(ex.getErrorMsg())
                        .detail(detail)
                        .build());
    }
}

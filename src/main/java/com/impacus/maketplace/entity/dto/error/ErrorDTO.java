package com.impacus.maketplace.entity.dto.error;

import com.impacus.maketplace.common.enumType.ErrorType;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDTO {

    private String code;
    private String msg;
    private String detail;

    public static ResponseEntity<ErrorDTO> to400ResponseEntity(ErrorType errorType, String detail) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDTO.builder()
                        .code(errorType.getCode())
                        .msg(errorType.getMsg())
                        .detail(detail)
                        .build());
    }
}

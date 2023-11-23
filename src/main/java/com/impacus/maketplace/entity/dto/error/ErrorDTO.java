package com.impacus.maketplace.entity.dto.error;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDTO {

    private String code;
    private String msg;
    private String detail;

    public static ResponseEntity<ErrorDTO> toResponseEntity(CustomException ex) {
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

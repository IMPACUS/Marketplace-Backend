package com.impacus.maketplace.common.handler;

import com.impacus.maketplace.common.exception.Custom400Exception;
import com.impacus.maketplace.entity.dto.error.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Custom400Exception.class)
    protected ResponseEntity<ErrorDTO> handleCustom400Exception(Custom400Exception ex) {
        return ErrorDTO.to400ResponseEntity(ex.getErrorType(), ex.getDetail());
    }
}

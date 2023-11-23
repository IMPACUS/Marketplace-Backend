package com.impacus.maketplace.common.handler;

import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.dto.error.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorDTO> handleCustom400Exception(CustomException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }
}

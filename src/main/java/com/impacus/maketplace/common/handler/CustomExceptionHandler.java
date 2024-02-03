package com.impacus.maketplace.common.handler;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.error.response.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustom400Exception(CustomException ex) {
        ErrorType errorType = ex.getErrorType();
        log.warn(
                String.format("http-status={%s} code={%s} msg={%s} detail={%s}", ex.getStatus().value(),
                        errorType.getCode(), errorType.getMsg(), ex.getDetail())
        );

        return ErrorDTO.toResponseEntity(ex);
    }
}

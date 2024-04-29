package com.impacus.maketplace.common.handler;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.error.response.ErrorDTO;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMsg = null;
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            String field = fieldError.getField();
            String errormessage = fieldError.getDefaultMessage();

            errorMsg = String.format("field: %s, message: %s", field, errormessage);
        }

        CustomException customException = new CustomException(CommonErrorType.INVALID_REQUEST_DATA, errorMsg);

        return ErrorDTO.toResponseEntity(customException);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMsg = String.format("property: %s, message: %s", ex.getPropertyName(), ex.getMessage());
        CustomException customException = new CustomException(CommonErrorType.INVALID_REQUEST_DATA, errorMsg);

        return ErrorDTO.toResponseEntity(customException);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        CustomException customException = new CustomException(CommonErrorType.INVALID_REQUEST_DATA, ex.getMessage());

        return ErrorDTO.toResponseEntity(customException);
    }
}

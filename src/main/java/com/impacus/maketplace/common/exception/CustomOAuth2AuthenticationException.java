package com.impacus.maketplace.common.exception;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
public class CustomOAuth2AuthenticationException extends OAuth2AuthenticationException {

    private final ErrorType errorType;

    public CustomOAuth2AuthenticationException(String errorCode) {
        super(errorCode);
        this.errorType = ErrorType.UNKNOWN;
    }

    public CustomOAuth2AuthenticationException(String errorCode, ErrorType errorType) {
        super(errorCode);
        this.errorType = errorType;
    }
}

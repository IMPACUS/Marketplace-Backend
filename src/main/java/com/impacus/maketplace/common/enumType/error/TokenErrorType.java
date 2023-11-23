package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorType {
    NONE(1, null),
    EXPIRED_TOKEN(2, ErrorType.EXPIRED_TOKEN),
    UNSUPPORTED_TOKEN(3, ErrorType.INVALID_TOKEN),
    INVALID_TOKEN(4, ErrorType.INVALID_TOKEN);

    private final int code;
    private final ErrorType errorType;

}

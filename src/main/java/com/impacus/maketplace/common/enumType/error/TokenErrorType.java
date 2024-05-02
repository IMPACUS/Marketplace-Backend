package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorType {
    NONE(1, null),
    EXPIRED_TOKEN(2, CommonErrorType.EXPIRED_TOKEN),
    UNSUPPORTED_TOKEN(3, CommonErrorType.INVALID_TOKEN),
    INVALID_TOKEN(4, CommonErrorType.INVALID_TOKEN);

    private final int code;
    private final CommonErrorType errorType;
}

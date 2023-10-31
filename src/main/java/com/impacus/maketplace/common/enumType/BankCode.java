package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankCode {
    UNKNOWN(1, "UNKNOWN");

    private final int code;
    private final String value;
}

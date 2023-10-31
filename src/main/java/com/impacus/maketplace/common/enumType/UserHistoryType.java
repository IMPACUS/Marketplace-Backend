package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserHistoryType {
    NONE(1, "NONE");

    private final int code;
    private final String value;
}

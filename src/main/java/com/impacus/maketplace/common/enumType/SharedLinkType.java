package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SharedLinkType {
    NONE("00", "알 수 없음");

    private final String code;
    private final String value;
}


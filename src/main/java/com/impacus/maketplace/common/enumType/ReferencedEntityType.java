package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReferencedEntityType {
    PRODUCT(0, "PRODUCT"),

    NONE(100, null);

    private final int code;
    private final String name;
}

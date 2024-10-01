package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GrantMethod {
    AUTO("자동"),
    MANUAL("수동");

    private final String value;
}

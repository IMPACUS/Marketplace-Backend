package com.impacus.maketplace.common.enumType.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE(1, "활성화 상태"),
    BLOCKED (2, "차단된 상태");

    private final int code;
    private final String value;
}

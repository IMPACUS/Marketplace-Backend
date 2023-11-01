package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    UNCERTIFIED_USER(1, "미인증 회원"),
    CERTIFIED_USER(2, "인증 회원"),
    ADMIN(3, "관리자");

    private final int code;
    private final String value;
}

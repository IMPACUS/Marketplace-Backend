package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ROLE_UNCERTIFIED_USER(1, "미인증 회원"),
    ROLE_CERTIFIED_USER(2, "인증 회원"),
    ROLE_ADMIN(3, "관리자");

    private final int code;
    private final String value;
}

package com.impacus.maketplace.common.enumType.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ROLE_UNCERTIFIED_USER(0, "미인증 회원"),
    ROLE_CERTIFIED_USER(1, "인증 회원"),
    ROLE_ADMIN(2, "관리자"),
    ROLE_APPROVED_SELLER(3, "승인된 판매자"),
    ROLE_UNAPPROVED_SELLER(4, "미승인된 판매자");

    private final int code;
    private final String value;
}

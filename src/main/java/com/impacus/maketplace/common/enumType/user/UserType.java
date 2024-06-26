package com.impacus.maketplace.common.enumType.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ROLE_UNCERTIFIED_USER(0, "미인증 회원"),
    ROLE_CERTIFIED_USER(1, "인증 회원"),
    ROLE_APPROVED_SELLER(2, "승인된 판매자"),
    ROLE_UNAPPROVED_SELLER(3, "미승인된 판매자"),
    ROLE_OWNER(4, "관리자(총관 권한)"),
    ROLE_PRINCIPAL_ADMIN(5, "관리자(전체 수정 권한)"),
    ROLE_ADMIN(6, "관리자(일부 수정 권한)");

    public static UserType getAdminRole(String accountType) {
        switch (accountType) {
            case "owner":
                return UserType.ROLE_OWNER;
            case "principal admin":
                return UserType.ROLE_PRINCIPAL_ADMIN;
            case "admin":
                return UserType.ROLE_ADMIN;
            default:
                return UserType.ROLE_ADMIN;
        }
    }

    private final int code;
    private final String value;
}

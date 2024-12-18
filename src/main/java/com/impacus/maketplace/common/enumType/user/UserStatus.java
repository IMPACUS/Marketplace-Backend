package com.impacus.maketplace.common.enumType.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE(0, "활성화 상태"),
    SUSPENDED(2, "정지"),
    DORMANT(2, "휴면 상태"),
    DEACTIVATED(3, "탈퇴 상태");

    private final int code;
    private final String value;
}

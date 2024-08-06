package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorType implements ErrorType {
    DEACTIVATED_USER("001_DEACTIVATED_USER", "탈퇴한 회원입니다."),
    SUSPENDED_USER("002_SUSPENDED_USER", "정지된 회원입니다."),
    NOT_EXISTED_USER("003_NOT_EXISTED_USER", "존재하지 않는 회원입니다.");

    private final String code;
    private final String msg;
}

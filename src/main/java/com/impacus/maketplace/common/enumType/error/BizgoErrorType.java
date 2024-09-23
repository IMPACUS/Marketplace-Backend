package com.impacus.maketplace.common.enumType.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BizgoErrorType implements ErrorType {
    COMMUNICATION_ERROR("001_COMMUNICATION_ERROR", "비즈고 인증 토큰값 리턴 통신에러입니다."),
    UNAUTHORIZED("002_UNAUTHORIZED", "비즈고 인증 권한이 없습니다(401)"),
    UNKNOWN_ERROR("003_UNKNOWN_ERROR", "비즈고 서버에러입니다.(500)");

    private final String code;
    private final String msg;
}

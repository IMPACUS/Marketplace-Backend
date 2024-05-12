package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointErrorType implements ErrorType{
    INVALID_USER_LEVEL("080_INVALID_USER_LEVEL", "유효하지 않는 유저 레벨입니다."),
    INVALID_SELECTED_LEVEL_TARGET("081_INVALID_SELECTED_LEVEL_TARGET", "유효하지 않는 등급 대상입니다.");

    private final String code;
    private final String msg;

    }

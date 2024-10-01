package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointErrorType implements ErrorType{
    INVALID_POINT("001_INVALID_POINT_MANAGE", "유효하지 않은 포인트 입니다."),
    INVALID_POINT_TYPE("002_INVALID_POINT_TYPE", "유효하지 않는 포인트 타입입니다."),
    INVALID_USER_LEVEL("080_INVALID_USER_LEVEL", "유효하지 않는 유저 레벨입니다."),
    INVALID_SELECTED_LEVEL_TARGET("081_INVALID_SELECTED_LEVEL_TARGET", "유효하지 않는 등급 대상입니다.");


    private final String code;
    private final String msg;

}

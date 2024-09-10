package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorType implements ErrorType {

    NOT_ENOUGH_POINT_AMOUNT("082_NOT_ENOUGH_POINT_AMOUNT", "포인트 잔액이 부족합니다.");

    private final String code;
    private final String msg;
}

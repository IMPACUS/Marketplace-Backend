package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorType implements ErrorType {

    NOT_ENOUGH_POINT_AMOUNT("082_NOT_ENOUGH_POINT_AMOUNT", "포인트 잔액이 부족합니다."),
    FAIL_GENERATE_ORDER_ID("090_1_FAIL_GENERATE_ORDER_ID", "ORDER ID 생성에 실패했습니다."),
    FAIL_GENERATE_PAYMENT_ID("090_2_FAIL_GENERATE_PAYMENT_ID", "PAEMENT ID 생성에 실패했습니다.");

    private final String code;
    private final String msg;
}

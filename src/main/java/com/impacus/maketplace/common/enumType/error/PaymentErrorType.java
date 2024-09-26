package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorType implements ErrorType {

    NOT_ENOUGH_POINT_AMOUNT("082_NOT_ENOUGH_POINT_AMOUNT", "포인트 잔액이 부족합니다."),
    FAIL_GENERATE_ORDER_ID("090_1_FAIL_GENERATE_ORDER_ID", "ORDER ID 생성에 실패했습니다."),
    FAIL_GENERATE_PAYMENT_ID("090_2_FAIL_GENERATE_PAYMENT_ID", "PAEMENT ID 생성에 실패했습니다."),

    NOT_FOUND_CHANNEL_KEY("091_1_NOT_FOUND_CHANNEL_KEY", "채널 키를 찾을 수 없습니다."),
    NOT_FOUND_ORDER_ID("091_2_NOT_FOUND_ORDER_ID", "주문 번호를 찾을 수 없습니다."),

    DUPLICATE_USE_USER_COUPON("092_1_DUPLICATE_USE_USER_COUPON", "쿠폰은 하나의 상품 혹은 주문에만 적용할 수 있습니다."),
    INVALID_USE_POINT("092_2_INVALID_USE_POINT", "잘못된 포인트 사용을 시도했습니다.");

    private final String code;
    private final String msg;
}

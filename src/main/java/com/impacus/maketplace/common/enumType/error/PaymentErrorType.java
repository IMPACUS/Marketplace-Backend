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
    NOT_FOUND_MATCHED_PRODUCT_OPTION("091_3_NOT_FOUND_MATCHED_PRODUCT_OPTION", "해당 상품의 id와 일치하는 option을 찾을 수 없습니다."),
    NOT_FOUND_MATCHED_PRODUCT_OPTION_HISTORY("091_4_NOT_FOUND_MATCHED_PRODUCT_OPTION_HISTORY", "해당 상품의 옵션 id와 일치하는 히스토리를 찾을 수 없습니다."),
    NOT_FOUND_MATCHED_SELLER("091_5_NOT_FOUND_MATCHED_SELLER", "해당 seller를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT("091_6_NOT_FOUND_PRODUCT", "해당 product id와 일치하는 상품을 찾을 수 없습니다."),
    NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID("091_7_NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID", "Payment event id를 통해서 payment order를 조회한 결과 일치하는 레코드가 존재하지 않습니다."),

    DUPLICATE_USE_USER_COUPON("092_1_DUPLICATE_USE_USER_COUPON", "쿠폰은 하나의 상품 혹은 주문에만 적용할 수 있습니다."),
    INVALID_USE_POINT("092_2_INVALID_USE_POINT", "잘못된 포인트 사용을 시도했습니다."),

    MISMATCH_SHOPPING_CART_SIZE("093_1_MISMATCH_SHOPPING_CART_SIZE", "장바구니 요소 수와 조회한 상품의 수가 일치하지 않습니다."),

    MISMATCH_TOTAL_AMOUNT("099_0_MISMATCH_TOTAL_AMOUNT", "계산한 총 금액이 일치하지 않습니다.");

    private final String code;
    private final String msg;
}

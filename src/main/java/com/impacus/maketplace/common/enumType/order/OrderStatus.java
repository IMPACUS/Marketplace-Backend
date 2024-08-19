package com.impacus.maketplace.common.enumType.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PENDING("PENDING", "주문 대기"),
    CONFIRMED("CONFIRMED", "주문 확정"),
    PARTIALLY_CONFIRMED("PARTIALLY_CONFIRMED", "부분 주문 확정"),
    RETURNED("RETURNED", "반품 처리"),
    EXCHANGED("EXCHANGED", "교환 처리"),
    ORDER_CANCELLED("ORDER_CANCELLED", "주문 취소"),
    PAYMENT_CANCELLED("PAYMENT_CANCELLED", "결제 취소");

    private final String code;
    private final String value;
}

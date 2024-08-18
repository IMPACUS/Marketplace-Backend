package com.impacus.maketplace.common.enumType.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    PENDING("결재 대기중", "사용자가 주문 처리를 요청하고 결제가 완료되지 않은 상태"),
    COMPLETED("주문 처리 완료", "결제가 완료된 상태"),
    FAIL("주문 처리 실패", "사용자의 결제 처리가 완료되지 않아서 주문이 취소된 상태");

    private final String code;
    private final String value;
}

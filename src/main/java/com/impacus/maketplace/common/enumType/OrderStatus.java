package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    COMPLETE_PAYMENT(1, "결제 완료"),
    WAITING_FOR_PAYMENT(2, "결제 대기"),
    DEPOSIT_COMPLETED(3, "입금 완료"),
    SOLD_OUT(4, "입금 대기중"),

    NONE(100, "알수 없음");

    private final int code;
    private final String value;
}

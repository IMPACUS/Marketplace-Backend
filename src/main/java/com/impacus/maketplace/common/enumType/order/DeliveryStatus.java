package com.impacus.maketplace.common.enumType.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {

    WAITING_FOR_DELIVERY(1, "배송 대기중"),
    DELIVERY_IN_PROGRESS(2, "배송 중"),
    DELIVERY_COMPLETED(3, "배송 완료"),
    DELIVERY_CANCELED(4, "배송 취소"),

    BEFORE_DELIVERY(5, "발송 전"),

    NONE(100, "알수 없음");

    private final int code;
    private final String value;
}

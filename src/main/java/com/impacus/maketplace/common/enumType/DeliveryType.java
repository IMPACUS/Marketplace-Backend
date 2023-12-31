package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryType {
    GENERAL_DELIVERY(1, "일반 배송"),
    PLUS_DELIVERY(2, "배송+"),

    NONE(100, "알수 없음");

    private final int code;
    private final String value;
}

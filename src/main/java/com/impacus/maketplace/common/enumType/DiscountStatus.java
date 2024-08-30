package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountStatus {
    DISCOUNT_PROGRESS(1, "할인 진행중"),
    DISCOUNT_STOP(2, "할인 중지");

    private final int code;
    private final String value;
}

package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountType {

    PERCENT("%", "퍼센트 할인 방식"),
    AMOUNT("원", "금액 할인 방식");

    private final String code;
    private final String value;
}

package com.impacus.maketplace.common.enumType;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType {

    AMOUNT("amount", "원"),
    PERCENTAGE("percent" , "%");


    private final String code;
    private final String unit;
}

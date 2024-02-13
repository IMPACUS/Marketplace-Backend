package com.impacus.maketplace.common.enumType;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CouponType {

    AMOUNT("amount", "원"),
    PERCENTAGE("percent" , "%"),
    UNKNOWN("", "");


    private final String code;
    private final String unit;

    public static CouponType fromCode(String code) {
        return Arrays.stream(CouponType.values()).filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst().orElse(UNKNOWN);
    }
}

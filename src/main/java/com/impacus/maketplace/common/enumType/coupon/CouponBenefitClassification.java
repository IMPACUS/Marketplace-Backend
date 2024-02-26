package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CouponBenefitClassification {

    AMOUNT("amount", "원"),
    PERCENTAGE("percent" , "%"),
    UNKNOWN("", "");


    private final String code;
    private final String unit;

    public static CouponBenefitClassification fromCode(String code) {
        return Arrays.stream(CouponBenefitClassification.values()).filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst().orElse(UNKNOWN);
    }
}

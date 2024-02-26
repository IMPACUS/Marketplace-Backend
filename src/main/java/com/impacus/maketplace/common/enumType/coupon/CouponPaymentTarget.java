package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponPaymentTarget {

    CPT_1("CPT_1", "모든 회원"),
    CPT_2("CPT_2", "선착순");

    private final String code;
    private final String value;
}

package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponCoverage {

    CC_1("CC_1", "모든 상품 / 브랜드"),
    CC_2("CC_2", "특정 브랜드");

    private final String code;
    private final String value;
}

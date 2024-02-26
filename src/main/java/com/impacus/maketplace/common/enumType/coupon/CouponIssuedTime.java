package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuedTime {

    CIT_1("CIT_1", "구매 후 1 주일 뒤"),
    CIT_2("CIT_2", "즉시 발급");

    private final String code;
    private final String value;
}

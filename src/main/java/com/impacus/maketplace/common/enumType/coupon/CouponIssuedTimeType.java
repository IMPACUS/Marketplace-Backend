package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuedTimeType implements CouponUtils.CommonFieldInterface {

    WEEK("WEEK", "구매 후 1 주일 뒤"),
    IMMEDIATE("IMMEDIATE", "즉시 발급");

    private final String code;
    private final String value;
}

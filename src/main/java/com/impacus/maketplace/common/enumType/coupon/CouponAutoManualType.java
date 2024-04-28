package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponAutoManualType implements CouponUtils.CommonFieldInterface{

    AUTO("AUTO", "자동 발급"),
    MANUAL("MANUAL", "수동 발급"),
    UNKNOWN("","");


    private final String code;
    private final String value;
}

package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssuanceStatus implements CouponUtils.CommonFieldInterface{
    ISSUING("ISSUING", "발급중"),
    ISSUED("ISSUED", "발급 됨"),
    STOP("STOP", "발급 중지"),
    UNKNOWN("","");

    private final String code;
    private final String value;

}

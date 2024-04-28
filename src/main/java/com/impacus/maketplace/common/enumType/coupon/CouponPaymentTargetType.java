package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponPaymentTargetType implements CouponUtils.CommonFieldInterface {

    ALL("ALL", "모든 회원"),
    FIRST("FIRST", "선착순");

    private final String code;
    private final String value;
}

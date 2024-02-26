package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuanceType implements CouponUtils.CommonFieldInterface{

    CIT_1("CIT_1", "자동 발급"),
    CIT_2("CIT_2", "수동 발급");

    private final String code;
    private final String value;
}

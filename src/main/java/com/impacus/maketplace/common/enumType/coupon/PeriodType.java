package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PeriodType implements CouponUtils.CommonFieldInterface {
    SET("SET", "지정 기간 설정"),
    UNSET("UNSET", "지정 기간 없음 (지속적인 기준)");


    private final String code;
    private final String value;
}

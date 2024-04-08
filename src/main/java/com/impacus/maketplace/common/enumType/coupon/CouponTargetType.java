package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponTargetType implements CouponUtils.CommonFieldInterface{

    USER("USER", "회원 검색"),
    ALL("ALL", "모든 회원"),
    UNKNOWN("", "");

    private final String code;
    private final String value;

}

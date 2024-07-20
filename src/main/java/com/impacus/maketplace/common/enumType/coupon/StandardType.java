package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StandardType implements CouponUtils.CommonFieldInterface{

    UNLIMITED("UNLIMITED", "가격 제한 없음"),
    LIMIT("LIMIT", "N원 이상 구매시");

    private final String code;
    private final String value;
}

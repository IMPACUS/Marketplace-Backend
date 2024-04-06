package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CouponType implements CouponUtils.CommonFieldInterface{

    EVENT("CT_1", "이벤트_실행형"),
    REQUITAL("CT_2", "지급형_일괄지급형");

    private final String code;
    private final String value;

}

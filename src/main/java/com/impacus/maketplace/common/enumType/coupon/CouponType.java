package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponType implements CouponUtils.CommonFieldInterface{

    EVENT("EVENT", "이벤트_실행형"),
    PROVISION("PROVISION", "지급형_일괄지급형");

    private final String code;
    private final String value;

}

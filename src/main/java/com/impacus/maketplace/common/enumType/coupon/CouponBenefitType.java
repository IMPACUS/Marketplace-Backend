package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponBenefitType  implements CouponUtils.CommonFieldInterface {

    AMOUNT("AMOUNT", "원"),
    PERCENTAGE("PERCENT" , "%"),
    UNKNOWN("", "");


    private final String code;
    private final String value;

}

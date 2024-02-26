package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CouponBenefitClassification  implements CouponUtils.CommonFieldInterface {

    AMOUNT("amount", "원"),
    PERCENTAGE("percent" , "%"),
    UNKNOWN("", "");


    private final String code;
    private final String value;

}

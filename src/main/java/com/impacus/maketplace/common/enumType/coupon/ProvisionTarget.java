package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProvisionTarget implements CouponUtils.CommonFieldInterface{

    USER("USER", "TARGET_USER"),
    ALL("ALL", "TARGET_ALL");

    private final String code;
    private final String value;
}

package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CoverageType implements CouponUtils.CommonFieldInterface{

    ALL("ALL", "모든 상품 / 브랜드"),
    BRAND("BRAND", "특정 브랜드");

    private final String code;
    private final String value;
}

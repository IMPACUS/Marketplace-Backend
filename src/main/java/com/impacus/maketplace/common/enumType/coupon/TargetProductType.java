package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetProductType implements CouponUtils.CommonFieldInterface {
    ECO_GREEN("ECO_GREEN", "에코/그린 상품"),
    BASIC("BASIC" , "일반상품"),
    ALL("ALL" , "구분없음");

    private final String code;
    private final String value;
}

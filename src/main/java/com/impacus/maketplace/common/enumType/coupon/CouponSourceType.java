package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponSourceType {

    ORDER("ORDER", "주문에 사용"),
    ORDER_PRODUCT("ORDER_PRODUCT", "주문한 상품에 사용"),
    NONE("NONE", "사용되지 않음");

    private final String code;
    private final String value;
}

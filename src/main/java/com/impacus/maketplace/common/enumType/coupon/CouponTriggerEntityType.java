package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponTriggerEntityType {
    ORDER("ORDER", "주문"),
    PRODUCT("PRODUCT", "구매 상품");

    private final String code;
    private final String value;
}

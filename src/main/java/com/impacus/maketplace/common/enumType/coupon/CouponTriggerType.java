package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponTriggerType {
    SIGNUP("SIGN_UP", "회원가입"),
    ORDER("ORDER", "주문"),
    PRODUCT("PRODUCT", "구매 상품"),
    SNS("SNS", "SNS 태그"),
    ADMIN("ADMIN", "ADMIN 지급");

    private final String code;
    private final String value;
}

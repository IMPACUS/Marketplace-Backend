package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TriggerType implements CouponUtils.CommonFieldInterface {
    SIGNUP("SIGNUP", "회원가입 시 발급되는 이벤트 쿠폰"),
    ORDER("ORDER", "주문별로 조건 만족 시 발급되는 이벤트 쿠폰"),
    PRODUCT("PRODUCT", "상품별로 조건 만족 시 발급되는 이벤트 쿠폰"),
    SNS("SNS", "SNS 태그를 통해 발급되는 이벤트 쿠폰");

    private final String code;
    private final String value;
}

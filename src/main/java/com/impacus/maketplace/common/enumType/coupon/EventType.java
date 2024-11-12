package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import com.impacus.maketplace.dto.coupon.request.CouponEventTypeDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType implements CouponUtils.CommonFieldInterface{

    SIGN_UP("SIGN_UP", "회원가입 시 발급되는 이벤트 쿠폰"),
    ORDER("ORDER", "주문별로 조건 만족 시 발급 되는 이벤트 쿠폰"),
    PRODUCT("PRODUCT", "상품별로 조건 만족 시 발급 되는 쿠폰"),
    SNS("SNS", "SNS 태그를 통해 발급되는 이벤트 쿠폰"),
    FIRST_COUNT("FIRST_COUNT","매일 00:00시부터 순차적으로 주문한 네명에게 제공되는 쿠폰");

    private final String code;
    private final String value;

    public CouponEventTypeDTO convert() {
        return new CouponEventTypeDTO(this.code, this.value);
    }
}

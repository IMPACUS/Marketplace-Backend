package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import com.impacus.maketplace.dto.coupon.request.CouponEventTypeDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType implements CouponUtils.CommonFieldInterface{

    SIGN_UP("SIGN_UP", "회원가입 시 발급되는 이벤트 쿠폰"),
    PAYMENT_ORDER("PAYMENT_ORDER", "주문별로 조건 만족 시 발급 되는 이벤트 쿠폰"),
    PAYMENT_PRODUCT("PAYMENT_PRODUCT", "상품별로 조건 만족 시 발급 되는 쿠폰"),
    SCHEDULING_PAYMENT_PRODUCT("SCHEDULING_PAYMENT_PRODUCT","매일 00:00시부터 순차적으로 N만원 이상 상품을 구매한 네명에게 제공되는 쿠폰"),
    SNS("SNS", "SNS 태그를 통해 발급되는 이벤트 쿠폰");

    private final String code;
    private final String value;

    public CouponEventTypeDTO convert() {
        return new CouponEventTypeDTO(this.code, this.value);
    }

    public static boolean isRelatedPaymentProduct(EventType eventType) {
        return eventType == PAYMENT_PRODUCT || eventType == SCHEDULING_PAYMENT_PRODUCT;
    }
}

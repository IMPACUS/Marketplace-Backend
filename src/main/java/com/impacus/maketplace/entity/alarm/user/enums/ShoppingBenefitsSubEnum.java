package com.impacus.maketplace.entity.alarm.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ShoppingBenefitsSubEnum {
    COUPON_EXTINCTION1("쿠폰 소멸1"),
    COUPON_EXTINCTION2("쿠폰 소멸2"),
    POINT_EXTINCTION1("포인트 소멸1"),
    POINT_EXTINCTION2("포인트 소멸2");
    private String value;
}

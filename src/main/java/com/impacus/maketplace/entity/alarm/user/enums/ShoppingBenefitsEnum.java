package com.impacus.maketplace.entity.alarm.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ShoppingBenefitsEnum {
    ADVERTISEMENT("광고"),
    COUPON_EXTINCTION("쿠폰 소멸"),
    POINT_EXTINCTION("포인트 소멸");
    private String value;
}

package com.impacus.maketplace.entity.alarm.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public enum ShoppingBenefitsEnum {
    ADVERTISEMENT("광고"),
    COUPON_EXTINCTION("쿠폰 소멸"),
    DORMANCY_INFORMATION("휴면 안내");
    private String value;
}

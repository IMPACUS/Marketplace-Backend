package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuanceClassification implements CouponUtils.CommonFieldInterface {

    GREEN_TAG("GREEN_TAG", "그린 태그 구매"),
    USER_BASIC("USER_BASIC", "유저 일반 쿠폰"),
    WELCOME_USER("WELCOME_USER", "신규 고객 첫 주문"),
    SNS("SNS", "SNS 홍보 태그"),
    UNKNOWN("", "");

    private final String code;
    private final String value;

}

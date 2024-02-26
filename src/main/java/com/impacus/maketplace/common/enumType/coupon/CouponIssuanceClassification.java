package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssuanceClassification implements CouponUtils.CommonFieldInterface {

    CIC_1("CIC_1", "그린 태그 구매"),
    CIC_2("CIC_2", "유저 일반 쿠폰"),
    CIC_3("CIC_3", "신규 고객 첫 주문"),
    CIC_4("CIC_4", "SNS 홍보 태그");

    private final String code;
    private final String value;

}

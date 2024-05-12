package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatusType implements CouponUtils.CommonFieldInterface{
    ISSUING("ISSUING", "발급 중"), // 쿠폰을 발급 되었으나, 사용자 한테 발급 되기 전 상태
    ISSUED("ISSUED", "발급 됨"),   // 사용자에게 발급이 된 상태
    STOP("STOP", "발급 중지"),  // 사용자에게 발급이 될 수 없는 상태, 관리자가 발급대기로 변경해야함
    UNKNOWN("","");

    private final String code;
    private final String value;

}

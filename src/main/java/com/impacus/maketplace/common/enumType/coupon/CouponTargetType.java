package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CouponTargetType {

    USER("user", "회원 검색"),
    ALL("all", "모든 회원"),
    UNKOWN("", "");

    private final String code;
    private final String value;

    public static CouponTargetType fromCode(String code){
        return Arrays.stream(CouponTargetType.values()).filter(t -> t.getCode().equals(code)).findFirst().orElse(UNKOWN);
    }
}

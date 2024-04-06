package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CouponProvideAlarmType {

    EMAIL_ALARM("email", "이메일 알림"),
    KAKAO_ALARM("kakaoTalk", "카카오톡 알림"),
    SMS_ALARM("sms", "문자 알림"),
    UNKOWN("", "");

    private final String code;
    private final String value;

    public static CouponProvideAlarmType fromCode(String code) {
        return Arrays.stream(CouponProvideAlarmType.values()).filter(t -> t.getCode().equals(code)).findFirst().orElse(UNKOWN);
    }
}

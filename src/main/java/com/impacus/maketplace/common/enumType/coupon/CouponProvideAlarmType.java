package com.impacus.maketplace.common.enumType.coupon;

import com.impacus.maketplace.common.utils.CouponUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponProvideAlarmType implements CouponUtils.CommonFieldInterface{

    EMAIL_ALARM("email", "이메일 알림"),
    KAKAO_ALARM("kakaoTalk", "카카오톡 알림"),
    SMS_ALARM("sms", "문자 알림"),
    UNKNOWN("", "");

    private final String code;
    private final String value;

}

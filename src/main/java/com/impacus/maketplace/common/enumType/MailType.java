package com.impacus.maketplace.common.enumType;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailType {
    AUTH("01", "auth_mail",  "인증번호 메일 입니다."),
    PASSWORD("02", "password_mail", "패스워드 메일 입니다."),
    POINT_REDUCTION("10", "points_reduction_mail", "5개월 휴면 메일입니다."),
    DORMANCY_INFO("11", "dormancy_info_mail", "11개월 휴면 메일 입니다."),
    USER_DELETE("12", "user_delete_mail", "14개월 휴면 메일 입니다."),
    EMAIL_VERIFICATION("13", "auth_mail", "이메일 인증"),
    UNKNOWN("99","", "");


    private final String code;
    private final String template;
    private final String subject;

    public static MailType fromCode(String code) {
        return Arrays.stream(MailType.values()).filter(t -> t.getCode().equals(code)).findFirst()
                .orElse(UNKNOWN);
    }


}

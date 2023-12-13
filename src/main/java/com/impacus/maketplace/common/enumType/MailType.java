package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MailType {
    AUTH("01", "auth_mail"),
    PASSWORD("02", "password_mail"),
    UNKNOWN("99","");


    private final String code;
    private final String template;

    public static MailType fromCode(String code) {
        return Arrays.stream(MailType.values()).filter(t -> t.getCode().equals(code)).findFirst()
                .orElse(UNKNOWN);
    }


}

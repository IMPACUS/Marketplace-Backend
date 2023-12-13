package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailType {
    AUTH(1, "auth_mail"),
    PASSWORD(2, "password_mail");


    private final int code;
    private final String template;

}

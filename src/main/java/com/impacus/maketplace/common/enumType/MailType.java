package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailType {
    AUTH(1, "AUTH"),
    PASSWORD(2, "PASSWORD");


    private final int code;
    private final String value;

}

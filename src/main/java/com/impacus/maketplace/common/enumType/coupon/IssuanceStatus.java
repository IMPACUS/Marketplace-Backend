package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum IssuanceStatus {
    ISSUING("issuing", "발급중"),
    ISSUED("issued", "발급 됨"),
    STOP("issued", "발급 중지"),
    UNKNOWN("","");

    private final String code;
    private final String value;

    public static IssuanceStatus fromCode(String code) {
        return Arrays.stream(IssuanceStatus.values()).filter(i -> i.getCode().equals(code))
                .findFirst().orElse(UNKNOWN);
    }

}

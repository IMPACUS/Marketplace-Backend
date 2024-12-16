package com.impacus.maketplace.common.enumType.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

    CARD("CARD", "CARD"),
    KAKAO_PAY("KAKAO_PAY", "EASY_PAY");

    private final String code;
    private final String value;
}

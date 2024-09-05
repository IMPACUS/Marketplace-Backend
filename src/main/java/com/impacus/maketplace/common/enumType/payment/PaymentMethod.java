package com.impacus.maketplace.common.enumType.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

    CARD("CARD", "카드 결제"),
    KAKAO_PAY("KAKAO_PAY", "카카오 페이");

    private final String code;
    private final String value;
}

package com.impacus.maketplace.common.enumType.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    NORMAL("NORMAL", "일반 결제");

    private final String code;
    private final String value;
}

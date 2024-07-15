package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CreditCardCompany {
    KB("01", "KB국민카드");

    private final String code;
    private final String value;
}

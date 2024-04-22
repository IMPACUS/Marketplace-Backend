package com.impacus.maketplace.common.enumType.seller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessType {
    GENERAL_TAXABLE_PERSON(1, "일반과세자"),
    SIMPLIFIED_TAXABLE_PERSON(2, "간이과세자"),
    CORPORATE_TAXPAYER(3, "법인사업자"),
    SOLE_PROPRIETOR(4, "개인사업자");

    private final int code;
    private final String value;
}

package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProvisionTarget {

    USER("1", "TARGET_USER"),
    ALL("2", "TARGET_ALL");

    private final String code;
    private final String value;
}

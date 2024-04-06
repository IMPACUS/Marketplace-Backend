package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProvisionTarget {

    USER("user", "TARGET_USER"),
    ALL("all", "TARGET_ALL");

    private final String code;
    private final String value;
}

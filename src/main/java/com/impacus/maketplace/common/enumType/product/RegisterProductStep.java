package com.impacus.maketplace.common.enumType.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegisterProductStep {
    BASIC(1, "기본 정보"),
    OPTIONS(2, "상품 옵션"),
    DETAILS(3, "상품 상세 정보");

    private final int step;
    private final String description;
}

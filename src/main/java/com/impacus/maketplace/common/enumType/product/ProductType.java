package com.impacus.maketplace.common.enumType.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {
    GENERAL(1, "일반 상품"),
    GREEN_TAG(2, "그린 태그");

    private final int code;
    private final String value;
}

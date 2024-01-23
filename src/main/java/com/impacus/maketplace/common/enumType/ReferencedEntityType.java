package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReferencedEntityType {
    PRODUCT(0, "Product"),
    PRODUCT_DESCRIPTION(1, "ProductDescription"),
    TEMPORARY_PRODUCT(2, "TemporaryProduct"),
    TEMPORARY_PRODUCT_DESCRIPTION(3, "TemporaryProductDescription"),

    NONE(100, null);

    private final int code;
    private final String name;
}

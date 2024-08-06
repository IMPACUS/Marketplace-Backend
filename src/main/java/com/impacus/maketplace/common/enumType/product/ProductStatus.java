package com.impacus.maketplace.common.enumType.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    SALES_PROGRESS(1, "판매 진행중"),
    SALES_STOP(2, "판매 중지"),
    SOLD_OUT(3, "품절"),

    NONE(100, "알수 없음");

    private final int code;
    private final String value;
}

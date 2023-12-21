package com.impacus.maketplace.common.enumType.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuperCategory {
    BRAND(1, "브랜드"),
    FASHION(2, "패션"),
    BAG(3, "가방"),
    ACCESSORY(4, "악세서리"),
    FURNITURE(5, "가정/가구"),
    LIFE_STYLE(6, "라이프 스타일"),
    ECO(7, "ECO");


    private final int code;
    private final String value;
}

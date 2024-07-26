package com.impacus.maketplace.common.enumType.seller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SellerType {

    BRAND(1, "브랜드"),
    UNOFFICIAL_AGENT(2, "비공식 중개 대행 서비스");

    private final int code;
    private final String value;
}

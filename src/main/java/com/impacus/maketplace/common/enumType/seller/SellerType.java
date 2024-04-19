package com.impacus.maketplace.common.enumType.seller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SellerType {
    UNKNOWN(1, "알 수 없음");

    private final int code;
    private final String value;
}

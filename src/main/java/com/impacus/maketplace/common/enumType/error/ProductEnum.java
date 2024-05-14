package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductEnum implements ErrorType {
    PRODUCT_ACCESS_DENIED("001_PRODUCT_ACCESS_DENIED", "판매자가 등록한 상품이 아닙니다. 접근할 수 있는 권한이 없습니다.");

    private final String code;
    private final String msg;
}
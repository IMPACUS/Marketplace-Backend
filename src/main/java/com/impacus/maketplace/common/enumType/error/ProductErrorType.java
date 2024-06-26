package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductErrorType implements ErrorType {
    PRODUCT_ACCESS_DENIED("001_PRODUCT_ACCESS_DENIED", "판매자가 등록한 상품이 아닙니다. 접근할 수 있는 권한이 없습니다."),
    INVALID_PRODUCT("011_INVALID_PRODUCT", "유효하지 않은 상품 데이터입니다."),
    NOT_EXISTED_PRODUCT("014_NOT_EXISTED_PRODUCT", "존재하지 않는 상품입니다."),
    NOT_EXISTED_TEMPORARY_PRODUCT("019_NOT_EXISTED_TEMPORARY_PRODUCT", "임시 저장 데이터가 존재하지 않습니다."),
    NOT_EXISTED_PRODUCT_OPTION("021_NOT_EXISTED_PRODUCT_OPTION", "존재하지 않는 상품 옵션입니다");

    private final String code;
    private final String msg;
}
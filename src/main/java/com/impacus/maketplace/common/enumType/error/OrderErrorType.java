package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorType implements ErrorType {

    OUT_OF_STOCK_ORDER_PRODUCT("060_OUT_OF_STOCK_ORDER_PRODUCT", "상품의 재고가 부족합니다."),
    DELETED_ORDER_PRODUCT("060_1_DELETED_ORDER_PRODUCT", "식제된 상품입니다."),
    SALE_STOP_ORDER_PRODUCT("060_2_SALE_STOP_ORDER_PRODUCT", "판매 중지된 상품입니다."),
    SOLD_OUT_ORDER_PRODUCT("060_3_SOLD_OUT_ORDER_PRODUCT", "품절된 상품입니다."),
    DELETED_ORDER_PRODUCT_OPTION("060_4_DELETED_ORDER_PRODUCT_OPTION", "주문한 상품 옵션이 삭제되었습니다.");




    private final String code;
    private final String msg;
}

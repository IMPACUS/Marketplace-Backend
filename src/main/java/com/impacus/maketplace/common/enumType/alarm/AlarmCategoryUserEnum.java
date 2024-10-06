package com.impacus.maketplace.common.enumType.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmCategoryUserEnum {
    ORDER_DELIVERY("주문/배송"),
    RESTOCK("재입고"),
    REVIEW("리뷰"),
    SERVICE_CENTER("고객센터"),
    BRAND_SHOP("브랜드 샵"),
    SHOPPING_BENEFITS("쇼핑혜택");
    private String value;
}

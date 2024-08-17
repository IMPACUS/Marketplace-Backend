package com.impacus.maketplace.common.enumType.product;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    SALES_PROGRESS(1, "판매 진행중"),
    SALES_STOP(2, "판매 중지"),
    SOLD_OUT(3, "품절");

    private final int code;
    private final String value;

    public static BooleanExpression containsEnumValue(EnumPath<ProductStatus> path, String keyword) {
        if (ProductStatus.SALES_PROGRESS.getValue().contains(keyword)) {
            return path.eq(ProductStatus.SALES_PROGRESS);
        } else if (ProductStatus.SALES_STOP.getValue().contains(keyword)) {
            return path.eq(ProductStatus.SALES_STOP);
        } else if (ProductStatus.SOLD_OUT.getValue().contains(keyword)) {
            return path.eq(ProductStatus.SOLD_OUT);
        } else {
            return null;
        }
    }

}

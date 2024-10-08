package com.impacus.maketplace.common.enumType.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    SALES_PROGRESS(1, "판매 진행중"),
    SALES_STOP(2, "판매 중지"),
    SOLD_OUT(3, "품절");

    private final int code;
    private final String value;

    public static BooleanExpression containsEnumValue(EnumPath<ProductStatus> path, String keyword) {
        List<ProductStatus> types = new ArrayList<>();

        if (ProductStatus.SALES_PROGRESS.getValue().contains(keyword)) {
            types.add(ProductStatus.SALES_PROGRESS);
        }
        if (ProductStatus.SALES_STOP.getValue().contains(keyword)) {
            types.add(ProductStatus.SALES_STOP);
        }
        if (ProductStatus.SOLD_OUT.getValue().contains(keyword)) {
            types.add(ProductStatus.SOLD_OUT);
        }

        return path.in(types);
    }
}

package com.impacus.maketplace.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatisticsCategory {
    VISITOR_COUNT, PRODUCT_CLICKS, ORDER_COUNT, DISCOUNT_AMOUNT,
    EXCHANGE_COUNT, REFUND_COUNT, ORDER_AMOUNT;
}

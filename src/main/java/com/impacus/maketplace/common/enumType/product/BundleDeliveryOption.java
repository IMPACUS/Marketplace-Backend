package com.impacus.maketplace.common.enumType.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BundleDeliveryOption {
    BUNDLE_DELIVERY_AVAILABLE(1, "묶음 배송 가능"),
    INDIVIDUAL_SHIPPING_ONLY(2, "개별 배송 상품");

    private final int code;
    private final String value;
}

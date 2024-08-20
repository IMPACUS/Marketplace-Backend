package com.impacus.maketplace.common.enumType.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryRefundType {
    FREE_SHIPPING(1, "무료 배송"),
    CHARGE_UNDER_30000(2, "구매 금액 3만 원 미만 시 배송비 부과"),
    STORE_DEFAULT(3, "스토어 배송 정보 설정에 따름");
    private final int code;
    private final String value;
}

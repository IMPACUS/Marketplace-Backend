package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BundleDeliveryGroupErrorType implements ErrorType {
    NOT_EXISTED_BUNDLE_DELIVERY_GROUP("001_NOT_EXISTED_BUNDLE_DELIVERY_GROUP", "존재하지 않은 묶음 배송 그룹입니다."),
    DUPLICATED_BUNDLE_DELIVERY_GROUP_NAME("002_DUPLICATED_BUNDLE_DELIVERY_GROUP_NAME", "중복 된 묶음 배송 그룹 명입니다.");

    private final String code;
    private final String msg;
}

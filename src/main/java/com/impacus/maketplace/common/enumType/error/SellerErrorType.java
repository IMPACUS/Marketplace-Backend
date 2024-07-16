package com.impacus.maketplace.common.enumType.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SellerErrorType implements ErrorType {
    NOT_EXISTED_SELLER_DELIVERY_ADDRESS_ID("001_NOT_EXISTED_SELLER_DELIVERY_ADDRESS_ID", "존재하지 않는 id입니다.");

    private final String code;
    private final String msg;
}

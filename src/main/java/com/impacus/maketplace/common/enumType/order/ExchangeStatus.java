package com.impacus.maketplace.common.enumType.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExchangeStatus {

    EXCHANGE_REQUESTED(1, "교환 요청"),
    EXCHANGE_ACCEPTED(2, "교환 승인"),
    EXCHANGE_REJECTED(3, "교환 거절"),
    EXCHANGE_COMPLETED(4, "교환 완료"),

    NONE(100, "알수 없음");

    private final int code;
    private final String value;
}

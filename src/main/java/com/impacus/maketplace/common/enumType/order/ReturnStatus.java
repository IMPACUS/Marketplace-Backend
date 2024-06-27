package com.impacus.maketplace.common.enumType.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReturnStatus {
    RETURN_REQUESTED(1, "반품 요청"),
    RETURN_ACCEPTED(2, "반품 승인"),
    RETURN_REJECTED(3, "반품 거절"),
    RETURN_COMPLETED(4, "반품 완료"),

    WITHDRAWAL_REQUESTED(5, "회수 요청"),
    WITHDRAWAL_COMPLETED(6, "회수 완료"),

    NONE(100, "알수 없음");

    private final int code;
    private final String value;
}

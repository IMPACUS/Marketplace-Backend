package com.impacus.maketplace.common.enumType.seller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntryStatus {
    REQUEST(1, "입점 요청"),
    APPROVE(2, "입점 승인"),
    REJECT(3, "입점 거절");

    private final int code;
    private final String value;
}

package com.impacus.maketplace.common.enumType.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InfoType {
    GREEN_TAG_COUPON(1),
    USER_COUPON(2),
    SNS_TAG_COUPON(3),
    POINT_REWARD(4),
    LEVEL_POINT(5);

    private final int code;
}

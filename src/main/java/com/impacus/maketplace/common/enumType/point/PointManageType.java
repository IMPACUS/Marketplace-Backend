package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PointManageType {
    PAYMENT("payment", "지급"),
    RECEIVE("receive","수취");

    private final String code;
    private final String value;
}

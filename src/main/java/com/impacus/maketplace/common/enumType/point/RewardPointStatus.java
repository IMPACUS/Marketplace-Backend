package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardPointStatus {
    ISSUING("발급중"),
    STOPPED("발급중지"),
    COMPLETED("지급완료");

    private final String value;
}

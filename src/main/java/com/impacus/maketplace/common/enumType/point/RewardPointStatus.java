package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardPointStatus {
    // 리워드 포인트
    ISSUING("발급중"),
    STOPPED("발급중지"),

    // 공통
    COMPLETED("지급완료"),

    // 포인트 지급
    FAILED("지급 실패"),
    MANUAL("수동");

    private final String value;
}

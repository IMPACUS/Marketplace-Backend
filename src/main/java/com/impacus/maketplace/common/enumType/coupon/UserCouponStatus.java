package com.impacus.maketplace.common.enumType.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCouponStatus {

    ISSUE_SUCCESS("지급 완료", "사용자가 다운로드 받을 수 있는 상태"),
    ISSUE_FAIL("지급 실패", "사용자가 환불 처리와 같은 이유로 발급이 실패된 상태"),
    ISSUE_PENDING("지급 대기중", "쿠폰 발급이 확정되지 않는 상태"),
    ISSUE_ERROR("중단", "서버의 내부적인 에러 로직으로 인해 발급되지 않은 상태");

    private final String code;
    private final String value;
}

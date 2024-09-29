package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    PURCHASE_GENERAL_PRODUCT(0, "일반 상품 구매", 50L),
    PURCHASE_GREEN_TAG_PRODUCT(1, "그린 라벨 상품 구매", 150L),
    USE(2, "결제 시 사용", 0L),
    EXPIRE(3, "소멸", 0L),
    CHECK(4, "출석체크", 30L),
    DORMANCY(5, "휴면에 의한 포인트 감소", 0L),
    ADMIN_PROVIDE(6, "관리자에 의한 포인트 지급", 0L),
    ADMIN_RECEIVE(7, "관리자에 의한 포인트 감소", 0L),
    REFUND_PRODUCT(8, "상품 환불", 0L),
    UPGRADE_LEVEL(9, "레벨 포인트 등급 상승", 0L),
    DOWNGRADE_LEVEL(10, "레벨 포인트 등급 하락", 0L),
    REVIEW(11, "리뷰 작성", 200L),
    SHARE_APP(12, "앱 공유", 200L),
    SHARE_PRODUCT(13, "상품 공유", 200L),
    SNS_TAG(14, "SNS 태그", 1000L),
    ECO_VIP_QUARTERLY_POINT(15, "ECO VIP 레벨 정기 포인트 지급", 50000L);

    private final int code;
    private final String value;
    private final Long allocatedPoints;
}

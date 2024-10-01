package com.impacus.maketplace.common.enumType.point;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    PURCHASE_GENERAL_PRODUCT(0, "일반 상품 구매", RewardPointType.PURCHASE_GENERAL_PRODUCT),
    PURCHASE_GREEN_TAG_PRODUCT(1, "그린 라벨 상품 구매", RewardPointType.PURCHASE_GREEN_TAG_PRODUCT),
    USE(2, "결제 시 사용", null),
    EXPIRE(3, "소멸", null),
    CHECK(4, "출석체크", RewardPointType.CHECK),
    DORMANCY(5, "휴면에 의한 포인트 감소", null),
    ADMIN_PROVIDE(6, "관리자에 의한 포인트 지급", RewardPointType.ADMIN_PROVIDE),
    ADMIN_RECEIVE(7, "관리자에 의한 포인트 감소", null),
    REFUND_PRODUCT(8, "상품 환불", null),
    UPGRADE_LEVEL(9, "레벨 포인트 등급 상승", null),
    DOWNGRADE_LEVEL(10, "레벨 포인트 등급 하락", null),
    REVIEW(11, "리뷰 작성", RewardPointType.REVIEW),
    SHARE_APP(12, "앱 공유", RewardPointType.SHARE_APP),
    SHARE_PRODUCT(13, "상품 공유", RewardPointType.SHARE_PRODUCT),
    SNS_TAG(14, "SNS 태그", RewardPointType.SNS_TAG),
    ECO_VIP_QUARTERLY_POINT(15, "ECO VIP 레벨 정기 포인트 지급", null),
    COUPON(16, "쿠폰 포인트 지급", RewardPointType.COUPON);

    private final int code;
    private final String value;

    /**
     * 리워드 포인트 타입
     * - 리워드 포인트가 아닌 경우 null
     */
    private final RewardPointType rewardPointType;
}

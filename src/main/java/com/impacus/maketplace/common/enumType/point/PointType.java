package com.impacus.maketplace.common.enumType.point;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum PointType {
    PURCHASE_PRODUCT(0, "상품 구매", RewardPointType.PURCHASE_PRODUCT),
    USE(2, "결제 시 사용", null),
    EXPIRE(3, "소멸", null),
    CHECK(4, "출석체크", RewardPointType.CHECK),
    DORMANCY(5, "휴면에 의한 포인트 감소", null),
    ADMIN_PROVIDE(6, "관리자에 의한 포인트 지급", RewardPointType.ADMIN_PROVIDE),
    ADMIN_RECEIVE(7, "관리자에 의한 포인트 감소", null),
    REFUND_PRODUCT(8, "상품 환불", null),
    UPGRADE_LEVEL(9, "레벨 포인트 등급 상승", RewardPointType.UPGRADE_LEVEL),
    DOWNGRADE_LEVEL(10, "레벨 포인트 등급 하락", null),
    REVIEW(11, "리뷰 작성", RewardPointType.REVIEW),
    SHARE_APP(12, "앱 공유", RewardPointType.SHARE_APP),
    SHARE_PRODUCT(13, "상품 공유", RewardPointType.SHARE_PRODUCT),
    SNS_TAG(14, "SNS 태그", RewardPointType.SNS_TAG),
    ECO_VIP_QUARTERLY_POINT(15, "ECO VIP 레벨 정기 포인트 지급", RewardPointType.UPGRADE_LEVEL),
    COUPON(16, "쿠폰 포인트 지급", RewardPointType.COUPON);

    private final int code;
    private final String value;

    /**
     * 리워드 포인트 타입
     * - 리워드 포인트가 아닌 경우 null
     */
    private final RewardPointType rewardPointType;

    public static List<PointType> getManualPointType() {
        List<PointType> pointTypes = new ArrayList<>();

        List<RewardPointType> manualRewardPointTypes = RewardPointType.getManualRewardPointTypes();
        for (PointType pointType : PointType.values()) {
            if (pointType.getRewardPointType() != null && manualRewardPointTypes.contains(pointType.getRewardPointType())) {
                pointTypes.add(pointType);
            }
        }

        return pointTypes;
    }

    public static BooleanExpression inEnumValue(EnumPath<PointType> path, RewardPointStatus rewardPointStatus) {
        List<PointType> manualPointTypes = PointType.getManualPointType();

        if (rewardPointStatus == RewardPointStatus.MANUAL) {
            return path.in(manualPointTypes);
        } else if (rewardPointStatus == RewardPointStatus.COMPLETED) {
            return path.notIn(manualPointTypes);
        }

        return path.in(new ArrayList<>());
    }

    public static BooleanExpression containsEnumValue(EnumPath<PointType> path, String keyword) {
        List<PointType> types = new ArrayList<>();

        for (PointType type : PointType.values()) {
            if (type.getValue().contains(keyword)) {
                types.add(type);
            }
        }

        return path.in(types);
    }
}

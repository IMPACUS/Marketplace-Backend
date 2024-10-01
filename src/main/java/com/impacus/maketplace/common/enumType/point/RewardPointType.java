package com.impacus.maketplace.common.enumType.point;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum RewardPointType {
    CHECK(1, "출석체크", 30L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    REVIEW(2, "리뷰 작성", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SHARE_APP(3, "앱 공유", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SHARE_PRODUCT(4, "상품 공유", 200L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    SNS_TAG(5, "SNS 태그", 1000L, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    PURCHASE_GREEN_TAG_PRODUCT(6, "그린 라벨 상품 구매", 150L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    PURCHASE_GENERAL_PRODUCT(7, "일반 상품 구매", 50L, Duration.ofDays(6 * 30), GrantMethod.AUTO),
    COUPON(7, "쿠폰 포인트 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL),
    ADMIN_PROVIDE(8, "관리자에 의한 포인트 지급", null, Duration.ofDays(6 * 30), GrantMethod.MANUAL);

    private final int code;
    private final String value;
    private final Long allocatedPoints; // 지급 포인트가 고정이 아닌 경우 null
    private final Duration expirationPeriod;
    private final GrantMethod grantMethod;

    public static BooleanExpression containsEnumValue(EnumPath<RewardPointType> path, String keyword) {
        List<RewardPointType> types = new ArrayList<>();

        for (RewardPointType type : RewardPointType.values()) {
            if (type.getValue().contains(keyword)) {
                types.add(type);
            }
        }

        return path.in(types);
    }
}

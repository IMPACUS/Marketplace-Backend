package com.impacus.maketplace.service.coupon.utils;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.entity.coupon.Coupon;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class CouponPrioritySelector {

    /**
     * <h3>발급 가능한 쿠폰 중 정책상 정해진 우선순위에 따라 1개의 쿠폰을 선정하는 함수</h3>
     * <p>우선 순위</p>
     * <ol>
     *     <li>혜택 구분이 할인 금액(AMOUNT)인 쿠폰 중 할인 금액이 가장 높은 쿠폰</li>
     *     <li>혜택 구분이 할인율(PERCENTAGE)인 쿠폰 중 할인율이 가장 높은 쿠폰</li>
     *     <li>동일 우선순위인 경우에는 (추후 추가 조건 또는 랜덤 선정)</li>
     * </ol>
     *
     * @param coupons 발급 가능한 쿠폰 리스트
     * @return 우선순위에 따른 단일 쿠폰, 만약 리스트가 비어있으면 null
     */
    public Coupon selectHighestPriorityCoupon(List<Coupon> coupons) {
        return coupons.stream().min(new CouponPriorityComparator()).orElse(null);
    }

    /**
     * CouponPriorityComparator는 쿠폰의 우선순위를 비교합니다.
     * <p>우선순위</p>
     * <ol>
     * <li>혜택 구분이 다르면, AMOUNT 타입이 우선 순위가 높습니다.</li>
     * <li>같은 혜택 구분이면, benefitValue가 높은 쿠폰이 우선입니다.</li>
     * </ol>
     */
    private static class CouponPriorityComparator implements Comparator<Coupon> {
        @Override
        public int compare(Coupon o1, Coupon o2) {
            // 혜택 구분이 다르면
            if (o1.getBenefitType() != o2.getBenefitType()) {
                // AMOUNT 타입이면 더 높은 우선순위 (음수 리턴)
                return (o1.getBenefitType() == BenefitType.AMOUNT) ? -1 : 1;
            }
            // 같은 혜택 구분이면, benefitValue가 높은 것이 우선
            return Long.compare(o2.getBenefitValue(), o1.getBenefitValue());
        }
    }
}


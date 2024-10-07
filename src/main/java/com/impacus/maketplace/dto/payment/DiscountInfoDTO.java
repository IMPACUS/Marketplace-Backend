package com.impacus.maketplace.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountInfoDTO {
    private Long productId;
    private Long appSalesPrice;             // 앱 판매가
    private Long ecoDiscountAmount;          // 에코 할인 금액
    private Long productCouponDiscountAmount; // 개별 상품 쿠폰 할인 금액
    private Long orderCouponDiscountAmount;   // 주문 전체 쿠폰 할인 금액
    private Long pointDiscountAmount;         // 포인트 할인 금액
    private Long quantity;

    public void addPointDiscountAmount(Long amount) {
        this.pointDiscountAmount += amount;
    }
    public Long getDiscountedAmount() {
        return getNotDiscountedAmount()
                - getEcoDiscountAmount()
                - productCouponDiscountAmount
                - orderCouponDiscountAmount
                - pointDiscountAmount;
    }

    public boolean isNegativeAmount() {
        return getDiscountedAmount() < 0;
    }

    public Long reconcilePointDiscountAmount() {

        Long discountedAmount = getDiscountedAmount();

        if (discountedAmount >= 0) return 0L;

        long absDiscountedAmount = Math.abs(discountedAmount);
        if (absDiscountedAmount <= pointDiscountAmount) {
            pointDiscountAmount -= absDiscountedAmount;
            return absDiscountedAmount;
        } else {
            long remain = pointDiscountAmount;
            pointDiscountAmount = 0L;
            return remain;
        }
    }

    public Long getNotDiscountedAmount() {
        return appSalesPrice * quantity;
    }

    public Long getEcoDiscountAmount() {
        return ecoDiscountAmount * quantity;
    }

    public Long getFinalCouponDiscount() {
        if (productCouponDiscountAmount + orderCouponDiscountAmount > getEcoDiscountedAmount())
            return getEcoDiscountedAmount();

        return productCouponDiscountAmount + orderCouponDiscountAmount;
    }

    public Long getEcoDiscountedAmount() {
        return getNotDiscountedAmount() - getEcoDiscountAmount();
    }

    public Long getFinalAmount() {
        Long discountedAMount = getDiscountedAmount();

        if (discountedAMount < 0) return 0L;

        return discountedAMount;
    }
}

package com.impacus.maketplace.dto.coupon.response;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class IssueCouponHistoriesDTO {
    Page<IssueCouponHistoryDTO> issueCouponHistoires;
    Long totalCount;
    Long totalDiscountedPrice;
    Long totalDiscountedPriceCount;
    Long totalDiscountedPercent;
    Long totalDiscountedPercentCount;

    public IssueCouponHistoriesDTO(Page<IssueCouponHistoryDTO> issueCouponHistoires, Long totalCount, Long totalDiscountedPrice, Long totalDiscountedPriceCount, Long totalDiscountedPercent, Long totalDiscountedPercentCount) {
        this.issueCouponHistoires = issueCouponHistoires;
        this.totalCount = totalCount;
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.totalDiscountedPriceCount = totalDiscountedPriceCount;
        this.totalDiscountedPercent = totalDiscountedPercent;
        this.totalDiscountedPercentCount = totalDiscountedPercentCount;
    }
}

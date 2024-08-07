package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.enumType.coupon.ExpireTimeType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserCouponOverviewDTO {
    private Long userCouponId;
    private Long couponId;
    private String name;
    private String description;
    private BenefitType benefitType;
    private Long benefitValue;
    private Boolean isDownload; // 쿠폰 다운로드 여부
    private LocalDateTime downloadAt;   // 쿠폰을 다운로드 받은 날짜
    private Boolean isUsed; // 쿠폰 사용 여부
    private LocalDateTime usedAt;   // 쿠폰을 사용한 날짜
    private LocalDate expiredAt;
    private LocalDate availableDownloadAt;

    @QueryProjection
    public UserCouponOverviewDTO(Long userCouponId, Long couponId, String name, String description, BenefitType benefitType, Long benefitValue, Boolean isDownload, LocalDateTime downloadAt, Boolean isUsed, LocalDateTime usedAt, LocalDate expiredAt, LocalDate availableDownloadAt) {
        this.userCouponId = userCouponId;
        this.couponId = couponId;
        this.name = name;
        this.description = description;
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
        this.isDownload = isDownload;
        this.downloadAt = downloadAt;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
        this.expiredAt = expiredAt;
        this.availableDownloadAt = availableDownloadAt;
    }
}

package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.AutoManualType;
import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.coupon.ExpireTimeType;
import com.impacus.maketplace.common.enumType.coupon.StandardType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CouponListInfoDTO {
    private Long couponId;
    private String code;
    private String name;
    private StandardType issueConditionType;
    private Long issueConditionValue;
    private ExpireTimeType expireTimeType;
    private Integer expireTimeDays;
    private Long quantityIssued;
    private AutoManualType autoManualType;
    private CouponStatusType statusType;
    private LocalDateTime modifyAt;

    @QueryProjection
    public CouponListInfoDTO(Long id, String code, String name, StandardType issueConditionType, Long issueConditionValue, ExpireTimeType expireTimeType, Integer expireTimeDays, Long quantityIssued, AutoManualType autoManualType, CouponStatusType statusType, LocalDateTime modifyAt) {
        this.couponId = id;
        this.code = code;
        this.name = name;
        this.issueConditionType = issueConditionType;
        this.issueConditionValue = issueConditionValue;
        this.expireTimeType = expireTimeType;
        this.expireTimeDays = expireTimeDays;
        this.quantityIssued = quantityIssued;
        this.autoManualType = autoManualType;
        this.statusType = statusType;
        this.modifyAt = modifyAt;
    }
}

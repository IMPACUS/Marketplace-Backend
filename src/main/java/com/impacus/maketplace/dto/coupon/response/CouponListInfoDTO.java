package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.common.enumType.coupon.AutoManualType;
import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.coupon.ExpireTimeType;
import com.impacus.maketplace.common.enumType.coupon.StandardType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CouponListInfoDTO {
    private Long id;
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
}

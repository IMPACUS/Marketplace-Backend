package com.impacus.maketplace.dto.coupon.response;

import com.impacus.maketplace.entity.coupon.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponDetailDTO {
    private String code;
    private String name;
    private String description;
    private String benefitType;
    private int benefitValue;
    private String productTargetType;
    private String paymentTargetType;
    private Long firstCount;
    private String issuedTimeType;
    private String type;
    private String expireTimeType;
    private Long expireDays;
    private String issueCoverageType;
    private String useCoverageType;
    private String useStandardType;
    private int useStandardValue;
    private String issueStandardType;
    private int issueStandardValue;
    private String periodType;
    private String periodStartAt;
    private String periodEndAt;
    private Long numberOfPeriod;
    private String autoManualType;
    private String loginAlert;
    private String smsAlert;
    private String emailAlert;
    private String statusType;

    //TODO: copyHelper 로 대체 가능할지 테스트 해보기
    public static CouponDetailDTO entityToDto(Coupon entity) {
        return CouponDetailDTO.builder()
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .benefitType(entity.getBenefitType().getCode())
                .benefitValue(entity.getBenefitValue())
                .productTargetType(entity.getProductTargetType().getCode())
                .paymentTargetType(entity.getPaymentTargetType().getCode())
                .firstCount(entity.getFirstCount())
                .issuedTimeType(entity.getIssuedTimeType().getCode())
                .type(entity.getType().getCode())
                .expireTimeType(entity.getExpireTimeType().getCode())
                .expireDays(entity.getExpireDays())
                .issueCoverageType(entity.getIssueCoverageType().getCode())
                .useCoverageType(entity.getUseCoverageType().getCode())
                .useStandardType(entity.getUseStandardType().getCode())
                .useStandardValue(entity.getUseStandardValue())
                .issueStandardType(entity.getIssueStandardType().getCode())
                .issueStandardValue(entity.getIssueStandardValue())
                .periodType(entity.getPeriodType().getCode())
                .periodStartAt(entity.getPeriodStartAt() != null ? entity.getPeriodStartAt().toString() : null)
                .periodEndAt(entity.getPeriodEndAt() != null ? entity.getPeriodEndAt().toString() : null)
                .numberOfPeriod(entity.getNumberOfPeriod())
                .autoManualType(entity.getAutoManualType().getCode())
                .loginAlert(entity.getLoginAlert())
                .smsAlert(entity.getSmsAlert())
                .emailAlert(entity.getEmailAlert())
                .build();
    }


}

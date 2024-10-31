package com.impacus.maketplace.dto.coupon.model;

import com.impacus.maketplace.dto.coupon.response.AvailableCouponsDTO;
import com.impacus.maketplace.dto.payment.model.PaymentCouponDTO;
import com.impacus.maketplace.repository.coupon.querydsl.dto.PaymentUserCouponInfo;
import com.impacus.maketplace.repository.coupon.querydsl.dto.UserCouponInfoForCheckoutDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

@Getter
@AllArgsConstructor
public class ValidatedPaymentCouponInfosDTO {
    private Map<Long, List<PaymentCouponDTO>> productCoupons;
    private List<PaymentCouponDTO> orderCoupons;

    public static ValidatedPaymentCouponInfosDTO createEmptyValidatedCouponsDTO() {
        return new ValidatedPaymentCouponInfosDTO(Collections.emptyMap(), Collections.emptyList());
    }

    public static ValidateProductCouponInfoDTO fromDto(UserCouponInfoForCheckoutDTO dto) {
        return new ValidateProductCouponInfoDTO(dto.getProductType(), dto.getUseCoverageType(), dto.getUseCoverageSubCategoryName(), dto.getUseStandardType(), dto.getUseStandardValue());
    }

    public ValidateProductCouponInfoDTO fromDto(PaymentUserCouponInfo dto) {
        return new ValidateProductCouponInfoDTO(dto.getProductType(), dto.getUseCoverageType(), dto.getUseCoverageSubCategoryName(), dto.getUseStandardType(), dto.getUseStandardValue());
    }
}

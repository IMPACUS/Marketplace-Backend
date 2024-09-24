package com.impacus.maketplace.repository.coupon.querydsl;

import com.impacus.maketplace.repository.coupon.querydsl.dto.ProductPricingInfoDTO;

import java.util.List;

public interface CouponProductRepository {

    List<ProductPricingInfoDTO> findProductPricingInfoList(List<Long> productIdList);
}

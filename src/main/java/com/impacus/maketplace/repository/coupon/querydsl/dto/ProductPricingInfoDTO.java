package com.impacus.maketplace.repository.coupon.querydsl.dto;

import com.impacus.maketplace.common.enumType.product.ProductType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ProductPricingInfoDTO {
    private Long productId;
    private ProductType productType; // 상품 타입
    private int appSalesPrice; // 앱 판매가
    private String marketName;  // 브랜드명

    @QueryProjection
    public ProductPricingInfoDTO(Long productId, ProductType productType, int appSalesPrice, String marketName) {
        this.productId = productId;
        this.productType = productType;
        this.appSalesPrice = appSalesPrice;
        this.marketName = marketName;
    }
}

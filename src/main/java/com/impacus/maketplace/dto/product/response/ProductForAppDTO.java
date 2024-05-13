package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductForAppDTO {
    private Long productId;
    private String name;
    private List<AttachFileDTO> productImageList;
    private String brandName;
    private int appSalePrice; // 판매가
    private DeliveryType deliveryType;
    private int discountPrice; // 할인가
    private double discountRate; // 할인률

    @QueryProjection
    public ProductForAppDTO(
            Long productId,
            String name,
            String brandName,
            int appSalePrice,
            DeliveryType deliveryType,
            int discountPrice,
            List<AttachFileDTO> productImageList
    ) {
        this.productId = productId;
        this.name = name;
        this.brandName = brandName;
        this.appSalePrice = appSalePrice;
        this.deliveryType = deliveryType;
        this.discountPrice = discountPrice;
        this.productImageList = productImageList;
    }

    public void setProductImageList(List<AttachFileDTO> productImageList) {
        this.productImageList = productImageList;
    }
}

package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import lombok.Data;

import java.util.Set;

@Data
public class ProductDetailForWebDTO {
    private String name;
    private DeliveryType deliveryType;
    private Long categoryId;
    private int deliveryFee;
    private int refundFee;
    private int marketPrice;
    private int appSalesPrice;
    private int discountPrice;
    private int weight;
    private ProductType type;
    private ProductStatus productStatus;
    private String description;
    private ProductDetailInfoDTO productDetail;
    private ProductDeliveryTimeDTO deliveryTime;
    private Set<ProductOptionDTO> productOptions;
    private Set<AttachFileDTO> productImageList;
    private ProductClaimInfoDTO claim;
}

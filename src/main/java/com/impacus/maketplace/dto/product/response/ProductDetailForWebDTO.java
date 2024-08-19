package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProductDetailForWebDTO {
    private Long id;
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
    private List<String> productImages;
    private ProductClaimInfoDTO claim;
}

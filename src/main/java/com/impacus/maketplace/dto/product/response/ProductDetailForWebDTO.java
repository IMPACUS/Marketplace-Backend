package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProductDetailForWebDTO {
    private Long id;
    private String name;
    private String productNumber;
    private DeliveryType deliveryType;
    private Boolean isCustomProduct;
    private Long categoryId;
    private DeliveryRefundType deliveryFeeType;
    private DeliveryRefundType refundFeeType;
    private Integer deliveryFee;
    private Integer refundFee;
    private Integer specialDeliveryFee;
    private Integer specialRefundFee;
    private DeliveryCompany deliveryCompany;
    private BundleDeliveryOption bundleDeliveryOption;
    private Long bundleDeliveryGroupId;
    private List<String> productImages;
    private int marketPrice;
    private int appSalesPrice;
    private int discountPrice;
    private int weight;
    private ProductStatus productStatus;
    private String description;
    private ProductType type;
    private Integer salesChargePercent;
    private ProductDetailInfoDTO productDetail;
    private ProductDeliveryTimeDTO deliveryTime;
    private Set<ProductOptionDTO> productOptions;
    private ProductClaimInfoDTO claim;

}

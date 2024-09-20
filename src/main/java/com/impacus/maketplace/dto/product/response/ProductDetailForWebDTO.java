package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
public class ProductDetailForWebDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long version;

    public void setProductOption(Set<ProductOptionDTO> productOptions) {
        this.productOptions = productOptions;
    }

    public void updateCategoryIdNull() {
        this.categoryId = null;
    }

    public void updateBundleDeliveryGroupId() {
        this.bundleDeliveryGroupId = null;
    }
}

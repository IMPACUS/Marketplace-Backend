package com.impacus.maketplace.dto.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.*;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ProductDTO {
    @NotBlank
    @Size(max = 50)
    private String name;

    @ValidEnum(enumClass = DeliveryType.class)
    private DeliveryType deliveryType;

    @NotNull
    @JsonProperty("isCustomProduct")
    private Boolean isCustomProduct;

    @NotNull
    private Long categoryId;

    @NotNull
    @ValidEnum(enumClass = DeliveryRefundType.class)
    private DeliveryRefundType deliveryFeeType;

    @NotNull
    @ValidEnum(enumClass = DeliveryRefundType.class)
    private DeliveryRefundType refundFeeType;

    private Integer deliveryFee;

    private Integer refundFee;

    private Integer specialDeliveryFee;

    private Integer specialRefundFee;

    @ValidEnum(enumClass = DeliveryCompany.class)
    private DeliveryCompany deliveryCompany;

    @ValidEnum(enumClass = BundleDeliveryOption.class)
    private BundleDeliveryOption bundleDeliveryOption;

    private Long bundleDeliveryGroupId;

    @NotNull
    private Integer marketPrice;

    @NotNull
    private Integer appSalesPrice;

    @NotNull
    private Integer discountPrice;

    @NotNull
    private Integer weight;

    @ValidEnum(enumClass = ProductStatus.class)
    private ProductStatus productStatus;

    @NotBlank
    private String description;

    @ValidEnum(enumClass = ProductType.class)
    private ProductType type;

    private Integer salesChargePercent;

    @Valid
    @NotNull
    private CreateProductDetailInfoDTO productDetail;

    @Valid
    @NotNull
    private List<CreateProductOptionDTO> productOptions;

    @Valid
    @NotNull
    private CreateProductDeliveryTimeDTO deliveryTime;

    @Valid
    @NotNull
    private CreateClaimInfoDTO claim;
}

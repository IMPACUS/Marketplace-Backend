package com.impacus.maketplace.dto.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.BundleDeliveryOption;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class BasicStepProductDTO {
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

    private Integer salesChargePercent;

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
    private CreateProductDeliveryTimeDTO deliveryTime;

    @NotNull
    private List<String> productImages;

    @NotNull
    private Integer marketPrice;

    @NotNull
    private Integer appSalesPrice;

    @NotNull
    private Integer discountPrice;

    public TemporaryProduct toEntity() {
        return new TemporaryProduct(this);
    }
}

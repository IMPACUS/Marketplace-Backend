package com.impacus.maketplace.dto.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.*;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {

    private Long sellerId;

    @NotNull
    private boolean doesUseTemporaryProduct;

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
    private List<String> productImages;

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

    @NotNull
    private CreateProductDetailInfoDTO productDetail;

    @NotNull
    private List<CreateProductOptionDTO> productOptions;

    @NotNull
    private CreateProductDeliveryTimeDTO deliveryTime;

    @NotNull
    private CreateClaimInfoDTO claim;

    public Product toEntity(Long sellerId) {
        return new Product(sellerId, this);
    }

    public TemporaryProduct toTemporaryEntity() {
        return new TemporaryProduct(this);
    }

}

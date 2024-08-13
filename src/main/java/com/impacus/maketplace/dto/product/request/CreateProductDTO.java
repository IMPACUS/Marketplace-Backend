package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
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

    @NotBlank
    private String description;

    @ValidEnum(enumClass = DeliveryType.class)
    private DeliveryType deliveryType;

    @NotNull
    private Long categoryId;

    @NotNull
    private int deliveryFee;

    @NotNull
    private int refundFee;

    @NotNull
    private int marketPrice;

    private int appSalesPrice;

    private int discountPrice;

    @NotNull
    private int weight;

    @NotNull
    private CreateProductDetailInfoDTO productDetail;

    @NotNull
    private List<CreateProductOptionDTO> productOptions;

    @NotNull
    private CreateProductDeliveryTimeDTO deliveryTime;

    @NotNull
    private CreateClaimInfoDTO claim;

    @ValidEnum(enumClass = ProductStatus.class)
    private ProductStatus productStatus;

    @ValidEnum(enumClass = ProductType.class)
    private ProductType type;

    public Product toEntity(String productNumber, Long sellerId) {
        return new Product(productNumber, sellerId, this);
    }

    public TemporaryProduct toTemporaryEntity(Long sellerId) {
        return new TemporaryProduct(sellerId, this);
    }

}

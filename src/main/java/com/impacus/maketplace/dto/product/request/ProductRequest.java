package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductDescription;
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
public class ProductRequest {
    @NotNull
    private Long brandId; // TODO 판매자 설계된 이후에 요청한 판매자와 연결된 Brand를 가져오는 것으로 변경

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private DeliveryType deliveryType;

    @NotNull
    private SubCategory categoryType;

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
    private ProductDetailInfoRequest productDetail;

    @NotNull
    private List<ProductOptionRequest> productOptions;

    @NotNull
    private ProductStatus productStatus;

    public Product toEntity(String productNumber) {
        return new Product(productNumber, this);
    }

    public ProductDescription toEntity(Long productId) {
        return ProductDescription.builder()
                .productId(productId)
                .description(this.description)
                .build();
    }
}

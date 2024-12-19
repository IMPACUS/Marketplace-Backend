package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.entity.product.ProductOption;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class WebProductTableDetailDTO {
    private Long id;
    private String name;
    private String price;
    private String productNumber;
    private DeliveryType deliveryType;
    private ProductStatus productStatus;
    private long stock;
    private LocalDateTime createAt;
    private List<WebProductOptionDTO> options;
    private List<String> productImages;

    @QueryProjection
    public WebProductTableDetailDTO(
            Long id,
            String name,
            String price,
            String productNumber,
            DeliveryType deliveryType,
            ProductStatus productStatus,
            LocalDateTime createAt,
            List<ProductOption> productOptions,
            List<String> productImages
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productNumber = productNumber;
        this.deliveryType = deliveryType;
        this.productStatus = productStatus;
        this.createAt = createAt;
        this.productImages = productImages;
        long stock = 0;
        List<WebProductOptionDTO> options = new ArrayList<>();
        for (ProductOption productOption : productOptions) {
            stock += productOption.getStock();
            options.add(new WebProductOptionDTO(productOption.getId(), productOption.getColor(), productOption.getSize()));
        }
        this.options = options;
        this.stock = stock;
    }
}

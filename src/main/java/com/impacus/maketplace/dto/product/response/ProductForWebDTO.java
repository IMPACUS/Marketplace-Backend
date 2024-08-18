package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ProductForWebDTO {
    private Long id;
    private String name;
    private String price;
    private String productNumber;
    private DeliveryType deliveryType;
    private ProductStatus productStatus;
    private long stock;
    private LocalDateTime createAt;
    private List<ProductOptionForWebDTO> options;
    private List<AttachFileDTO> productImageList;

    @QueryProjection
    public ProductForWebDTO(
            Long id,
            String name,
            String price,
            String productNumber,
            DeliveryType deliveryType,
            ProductStatus productStatus,
            long stock,
            LocalDateTime createAt,
            List<ProductOptionForWebDTO> productOptionList,
            List<AttachFileDTO> productImageList
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productNumber = productNumber;
        this.deliveryType = deliveryType;
        this.productStatus = productStatus;
        this.stock = stock;
        this.createAt = createAt;
        this.options = productOptionList;
        this.productImageList = productImageList;
    }
}

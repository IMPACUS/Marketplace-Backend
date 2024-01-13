package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.product.ProductOption;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductForWebDTO {
    private Long id;
    private String name;
    private int price;
    private String productNumber;
    private DeliveryType deliveryType;
    private ProductStatus productStatus;
    private Long stock;
    private LocalDateTime createAt;
    private List<ProductOptionDTO> options;


//    @QueryProjection
//    public ProductForWebDTO(
//            Long id,
//            String name,
//            int price,
//            String productNumber,
//            DeliveryType deliveryType,
//            ProductStatus productStatus,
//            Long stock,
//            LocalDateTime createAt
//    ) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.productNumber = productNumber;
//        this.deliveryType = deliveryType;
//        this.productStatus = productStatus;
//        this.stock = stock;
//        this.createAt = createAt;
//
//    }

    @QueryProjection
    public ProductForWebDTO(Product product, List<ProductOption> productOptionList) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getAppSalesPrice();
        this.productNumber = product.getProductNumber();
        this.deliveryType = product.getDeliveryType();
        this.productStatus = product.getProductStatus();
        this.stock = 0L;
        this.createAt = product.getCreateAt();
        this.options = productOptionList.stream()
                .map(productOption -> new ProductOptionDTO(
                        productOption.getId(),
                        productOption.getColor(),
                        productOption.getSize()
                ))
                .collect(Collectors.toList());
    }
}

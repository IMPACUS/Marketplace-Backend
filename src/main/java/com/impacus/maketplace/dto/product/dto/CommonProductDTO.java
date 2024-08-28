package com.impacus.maketplace.dto.product.dto;

import java.util.List;

import com.impacus.maketplace.entity.product.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonProductDTO {
    private Long productId;
    private Long sellerId;
    private String name;
    private String productNumber;
    private List<String> productImages;

    public Product toEntity(UpdateProductDTO dto) {
        Product changedProduct = new Product(this);
        changedProduct.setProduct(dto);

        return changedProduct;
    }
}

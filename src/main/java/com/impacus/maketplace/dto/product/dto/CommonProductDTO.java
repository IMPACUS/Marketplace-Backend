package com.impacus.maketplace.dto.product.dto;

import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import com.impacus.maketplace.entity.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommonProductDTO {
    private Long productId;
    private Long sellerId;
    private String name;
    private String productNumber;
    private List<String> productImages;
    private long version;

    public Product toEntity(UpdateProductDTO dto) {
        Product changedProduct = new Product(this);
        changedProduct.setProduct(dto);

        return changedProduct;
    }
}

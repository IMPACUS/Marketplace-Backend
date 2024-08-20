package com.impacus.maketplace.dto.shoppingBasket.response;

import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShoppingBasketDetailDTO {
    private Long shoppingBasketId;
    private Long quantity;
    private LocalDateTime modifyAt;
    private ProductForAppDTO product;
    private ProductOptionDTO productOption;

    @QueryProjection
    public ShoppingBasketDetailDTO(
            Long shoppingBasketId,
            Long quantity,
            LocalDateTime modifyAt,
            ProductForAppDTO product,
            ProductOptionDTO productOption
    ) {
        this.shoppingBasketId = shoppingBasketId;
        this.quantity = quantity;
        this.modifyAt = modifyAt;
        this.product = product;
        this.productOption = productOption;
    }

    public void setProductImageList(List<AttachFileDTO> productImageList) {
        this.product.setProductImageList(productImageList);
    }
}

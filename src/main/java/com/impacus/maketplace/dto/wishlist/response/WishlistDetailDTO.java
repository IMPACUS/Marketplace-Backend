package com.impacus.maketplace.dto.wishlist.response;

import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.dto.common.response.AttachFileDTO;
import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class WishlistDetailDTO {
    private Long wishlistId;
    private ProductForAppDTO product;

    @QueryProjection
    public WishlistDetailDTO(
            Long wishlistId,
            Long productId,
            String name,
            String brandName,
            int appSalePrice,
            DeliveryType deliveryType,
            int discountPrice,
            List<AttachFileDTO> productImageList
    ) {
        this.wishlistId = wishlistId;
        this.product = new ProductForAppDTO(
                productId,
                name,
                brandName,
                appSalePrice,
                deliveryType,
                discountPrice,
                productImageList
        );
    }
}

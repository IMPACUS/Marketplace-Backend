package com.impacus.maketplace.dto.wishlist.response;

import com.impacus.maketplace.entity.product.Wishlist;
import lombok.Builder;

@Builder
public record WishlistDTO(Long id, Long productId) {
    public static WishlistDTO toDTO(Wishlist wishlist) {
        return new WishlistDTO(wishlist.getId(), wishlist.getProductId());
    }
}

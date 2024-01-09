package com.impacus.maketplace.dto.wishlist.request;

import com.impacus.maketplace.entity.product.Wishlist;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequest {
    @NotNull
    private Long productId;

    public Wishlist toEntity() {
        return Wishlist.builder()
                .productId(this.productId)
                .build();
    }
}

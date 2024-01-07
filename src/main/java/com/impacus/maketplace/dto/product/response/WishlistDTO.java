package com.impacus.maketplace.dto.product.response;

import lombok.Builder;

@Builder
public record WishlistDTO(Long id, Long productId) {
}

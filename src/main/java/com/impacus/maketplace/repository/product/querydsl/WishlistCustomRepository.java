package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.dto.wishlist.response.WishlistDetailDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WishlistCustomRepository {
    Slice<WishlistDetailDTO> findWishlistsByUserId(Long userId, Pageable pageable);

    String findMarketNameByWishlistId(Long wishlistId);
}

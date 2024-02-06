package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.dto.wishlist.response.WishlistDetailDTO;

import java.util.List;

public interface WishlistCustomRepository {
    List<WishlistDetailDTO> findAllWishListByUserId(Long userId);
}

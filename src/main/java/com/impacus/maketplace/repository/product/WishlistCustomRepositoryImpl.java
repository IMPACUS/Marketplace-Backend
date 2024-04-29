package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.dto.wishlist.response.WishlistDetailDTO;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QWishlist;
import com.impacus.maketplace.entity.seller.QSeller;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WishlistCustomRepositoryImpl implements WishlistCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QWishlist wishlist = QWishlist.wishlist;
    private final QProduct product = QProduct.product;
    private final QSeller seller = QSeller.seller;

    @Override
    public List<WishlistDetailDTO> findAllWishListByUserId(Long userId) {
        List<WishlistDetailDTO> wishlistDetailDTOS = queryFactory
                .select(
                        Projections.constructor(
                                WishlistDetailDTO.class,
                                wishlist.id,
                                product.id,
                                product.name,
                                seller.marketName,
                                product.appSalesPrice,
                                product.deliveryType,
                                product.discountPrice
                        )
                )
                .from(wishlist)
                .leftJoin(product).on(product.id.eq(wishlist.id))
                .leftJoin(seller).on(seller.userId.eq(userId))
                .where(wishlist.registerId.eq(userId.toString()))
                .fetch();
        return wishlistDetailDTOS;
    }
}

package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.dto.product.dto.CommonProductDTO;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.product.*;
import com.impacus.maketplace.entity.seller.QSeller;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WebProductCustomRepositoryImpl implements WebProductCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;
    private final QSubCategory subCategory = QSubCategory.subCategory;
    private final QSeller seller = QSeller.seller;
    private final QWishlist wishlist = QWishlist.wishlist;
    private final QProductDeliveryTime productDeliveryTime = QProductDeliveryTime.productDeliveryTime;
    private final QProductDetailInfo productDetailInfo = QProductDetailInfo.productDetailInfo;
    private final QProductClaimInfo productClaimInfo = QProductClaimInfo.productClaimInfo;

    @Override
    public CommonProductDTO findProductByProductId(Long productId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(product.id.eq(productId))
                      .and(product.isDeleted.eq(false));

        return queryFactory
                .select(
                        Projections.fields(
                                CommonProductDTO.class,
                                product.id.as("productId"),
                                product.name,
                                product.sellerId,
                                product.productNumber,
                                product.productImages,
                                product.version
                        )
                )
                .from(product)
                .where(booleanBuilder)
                .fetchFirst();
    }

}

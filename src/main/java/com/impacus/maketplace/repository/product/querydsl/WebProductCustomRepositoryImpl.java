package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.dto.product.dto.CommonProductDTO;
import com.impacus.maketplace.dto.product.response.ProductClaimInfoDTO;
import com.impacus.maketplace.dto.product.response.ProductDeliveryTimeDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailForWebDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailInfoDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.product.response.WebProductDTO;
import com.impacus.maketplace.entity.category.QSubCategory;
import com.impacus.maketplace.entity.product.*;
import com.impacus.maketplace.entity.seller.QSeller;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WebProductCustomRepositoryImpl implements WebProductCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;
    private final QWishlist wishlist = QWishlist.wishlist;
    private final QProductDeliveryTime productDeliveryTime = QProductDeliveryTime.productDeliveryTime;
    private final QProductDetailInfo productDetailInfo = QProductDetailInfo.productDetailInfo;
    private final QProductClaimInfo productClaimInfo = QProductClaimInfo.productClaimInfo;

    @Override
    public CommonProductDTO findCommonProductByProductId(Long productId) {
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

    @Override
    public WebProductDTO findProductByProductId(UserType userType, Long sellerId, Long productId) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.id.eq(productId))
            .and(product.isDeleted.eq(false));

        if (userType == UserType.ROLE_APPROVED_SELLER) {
            productBuilder.and(product.sellerId.eq(sellerId));
        }

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(productOption.productId.eq(product.id))
            .and(productOption.isDeleted.eq(false));

        List<WebProductDTO> dtos =
            queryFactory
                .selectFrom(product)
                .leftJoin(productOption).on(productOptionBuilder)
                .leftJoin(wishlist).on(wishlist.productId.eq(product.id))
                .where(productBuilder)
                .groupBy(product.id, productOption.id)
                .transform(
                    GroupBy.groupBy(product.id).list(
                        Projections.constructor(
                            WebProductDTO.class,
                            product.id,
                            product.productNumber,
                            product.name,
                            product.productImages,
                            product.productStatus,
                            wishlist.count(),
                            product.appSalesPrice,
                            product.discountPrice,
                            GroupBy.list(
                                Projections.constructor(
                                    ProductOptionDTO.class,
                                    productOption.id,
                                    productOption.color,
                                    productOption.size
                                )
                            )
                        )
                    )
                );

        return dtos.isEmpty() ? null : dtos.get(0);
    }

}

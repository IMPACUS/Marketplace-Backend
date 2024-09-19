package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.utils.PaginationUtils;
import com.impacus.maketplace.dto.product.response.AppProductDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.ProductShoppingBasketDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDTO;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.product.QShoppingBasket;
import com.impacus.maketplace.entity.product.bundleDelivery.QBundleDeliveryGroup;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.seller.deliveryCompany.QSellerDeliveryCompany;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShoppingBasketCustomRepositoryImpl implements ShoppingBasketCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QShoppingBasket shoppingBasket = QShoppingBasket.shoppingBasket;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;
    private final QSeller seller = QSeller.seller;
    private final QSellerDeliveryCompany sellerDeliveryCompany = QSellerDeliveryCompany.sellerDeliveryCompany;
    private final QBundleDeliveryGroup bundleDeliveryGroup = QBundleDeliveryGroup.bundleDeliveryGroup;

    @Override
    public List<ProductShoppingBasketDTO> findAllShoppingBasketByUserId(Long userId) {
        return getShoppingBasketDetailDTOs(userId);
    }

    private List<ProductShoppingBasketDTO> getShoppingBasketDetailDTOs(Long userId) {
        BooleanBuilder productBuilder = new BooleanBuilder();
        productBuilder.and(product.id.eq(productOption.productId))
                .and(product.isDeleted.eq(false));

        BooleanBuilder productOptionBuilder = new BooleanBuilder();
        productOptionBuilder.and(shoppingBasket.productOptionId.eq(productOption.id))
                .and(productOption.isDeleted.eq(false));

        return queryFactory
                .selectFrom(shoppingBasket)
                .leftJoin(productOption).on(productOptionBuilder)
                .innerJoin(product).on(productBuilder)
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(sellerDeliveryCompany).on(sellerDeliveryCompany.sellerId.eq(seller.id))
                .leftJoin(bundleDeliveryGroup).on(product.bundleDeliveryGroupId.isNotNull().and(bundleDeliveryGroup.id.eq(product.bundleDeliveryGroupId)))
                .where(shoppingBasket.userId.eq(userId))
                .orderBy(shoppingBasket.modifyAt.desc())
                .transform(
                        GroupBy.groupBy(shoppingBasket.id)
                                .list(
                                        Projections.constructor(
                                        ProductShoppingBasketDTO.class,
                                        shoppingBasket.id,
                                        product.id,
                                        product.name,
                                        product.productImages,
                                        seller.marketName,
                                        product.deliveryFee,
                                        Projections.constructor(
                                                ProductOptionDTO.class,
                                                productOption.id,
                                                productOption.color,
                                                productOption.size
                                        ),
                                        product.deliveryType,
                                        product.type,
                                        product.discountPrice,
                                        product.bundleDeliveryGroupId,
                                        product.deliveryFeeType,
                                        sellerDeliveryCompany.generalDeliveryFee,
                                        shoppingBasket.quantity,
                                        shoppingBasket.modifyAt,
                                        bundleDeliveryGroup.deliveryFeeRule
                                )
                        )
                );
    }
}

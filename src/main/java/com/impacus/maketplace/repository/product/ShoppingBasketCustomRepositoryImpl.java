package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.dto.product.response.ProductForAppDTO;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.shoppingBasket.response.ShoppingBasketDetailDTO;
import com.impacus.maketplace.entity.QBrand;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.product.QShoppingBasket;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShoppingBasketCustomRepositoryImpl implements ShoppingBasketCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QShoppingBasket shoppingBasket = QShoppingBasket.shoppingBasket;
    private final QProduct product = QProduct.product;
    private final QProductOption productOption = QProductOption.productOption;
    private final QBrand brand = QBrand.brand;

    @Override
    public List<ShoppingBasketDetailDTO> findAllShoppingBasketByUserId(Long userId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ShoppingBasketDetailDTO.class,
                                shoppingBasket.id,
                                shoppingBasket.quantity,
                                Projections.constructor(
                                        ProductForAppDTO.class,
                                        product.id,
                                        product.name,
                                        brand.name,
                                        product.appSalesPrice,
                                        product.deliveryType,
                                        product.discountPrice
                                ),
                                Projections.constructor(
                                        ProductOptionDTO.class,
                                        productOption.id,
                                        productOption.color,
                                        productOption.size
                                )
                        )
                )
                .from(shoppingBasket)
                .leftJoin(productOption).on(shoppingBasket.productOptionId.eq(productOption.id))
                .leftJoin(product).on(product.id.eq(productOption.productId))
                .leftJoin(brand).on(brand.id.eq(product.brandId))
                .where(shoppingBasket.registerId.eq(userId.toString()))
                .fetch();
    }
}

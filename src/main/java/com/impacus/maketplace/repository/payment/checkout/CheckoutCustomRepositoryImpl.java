package com.impacus.maketplace.repository.payment.checkout;

import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.product.QShoppingBasket;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.QCheckoutProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.QCheckoutProductWithDetailsDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CheckoutCustomRepositoryImpl implements CheckoutCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;

    private final QProductOption productOption = QProductOption.productOption;
    private final QSeller seller = QSeller.seller;
    private final QShoppingBasket shoppingBasket = QShoppingBasket.shoppingBasket;

    @Override
    public CheckoutProductWithDetailsDTO findCheckoutProductWithDetails(Long productId, Long productOptionId) {
        return queryFactory
                .select(new QCheckoutProductWithDetailsDTO(
                        product.name,
                        product.productStatus,
                        product.type,
                        product.discountStatus,
                        product.appSalesPrice,
                        product.discountPrice,
                        product.deliveryFee,
                        product.productImages,
                        product.isDeleted,
                        seller.marketName,
                        productOption.color,
                        productOption.size,
                        productOption.stock,
                        productOption.isDeleted
                ))
                .from(product)
                .join(seller).on(seller.id.eq(product.sellerId))
                .join(productOption).on(productOption.id.eq(productOptionId))
                .where(product.id.eq(productId))
                .fetchOne();
    }

    @Override
    public List<CheckoutProductWithDetailsByCartDTO> findCheckoutProductWithDetailsByCart(List<Long> shoppingBasketIdList) {
        return queryFactory
                .select(new QCheckoutProductWithDetailsByCartDTO(
                        product.id,
                        product.name,
                        product.productStatus,
                        product.type,
                        product.discountStatus,
                        product.appSalesPrice,
                        product.discountPrice,
                        product.deliveryFee,
                        product.productImages,
                        product.isDeleted,
                        seller.marketName,
                        productOption.color,
                        productOption.size,
                        productOption.stock,
                        productOption.isDeleted,
                        productOption.id,
                        shoppingBasket.quantity
                ))
                .from(shoppingBasket)
                .join(productOption).on(productOption.id.eq(shoppingBasket.productOptionId))
                .join(product).on(product.id.eq(productOption.productId))
                .join(seller).on(seller.id.eq(product.sellerId))
                .where(shoppingBasket.id.in(shoppingBasketIdList))
                .fetch();
    }
}

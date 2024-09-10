package com.impacus.maketplace.repository.payment.checkout;

import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.product.QShoppingBasket;
import com.impacus.maketplace.entity.product.history.QProductOptionHistory;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.user.QUser;
import com.impacus.maketplace.repository.payment.checkout.dto.*;
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
    private final QProductOptionHistory productOptionHistory = QProductOptionHistory.productOptionHistory;
    private final QSeller seller = QSeller.seller;
    private final QShoppingBasket shoppingBasket = QShoppingBasket.shoppingBasket;
    private final QUser user = QUser.user;

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
                        seller.id,
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
                        seller.id,
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

    @Override
    public BuyerInfoDTO getBuyerInfo(Long userId) {
        return queryFactory
                .select(new QBuyerInfoDTO(
                        user.id,
                        user.email,
                        user.name,
                        user.phoneNumber
                ))
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();
    }

    /**
     * 추후 카드 등록 관련 로직 추가시 수정
     */
    @Override
    public PaymentProductInfoDTO getPaymentProductInfo(Long productId, Long productOptionId, Long sellerId, Boolean usedRegisteredCard, Long registeredCardId) {
        return queryFactory
                .select(new QPaymentProductInfoDTO(
                        product.id,
                        seller.id,
                        seller.chargePercent,
                        product.name,
                        product.productStatus,
                        product.appSalesPrice,
                        product.discountPrice,
                        product.deliveryFee,
                        product.isDeleted,
                        productOption.id,
                        productOption.color,
                        productOption.size,
                        productOption.stock,
                        productOption.isDeleted,
                        productOptionHistory.id
                ))
                .from(product)
                .join(seller).on(seller.id.eq(sellerId))
                .join(productOption).on(productOption.id.eq(productOptionId))
                .join(productOptionHistory).on(productOptionHistory.productOptionId.eq(productOptionId))
                .where(product.id.eq(productId))
                .fetchOne();
    }
}

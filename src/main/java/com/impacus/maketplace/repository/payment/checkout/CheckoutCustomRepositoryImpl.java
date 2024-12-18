package com.impacus.maketplace.repository.payment.checkout;

import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.payment.model.PaymentProductInfoIdDTO;
import com.impacus.maketplace.entity.product.QProduct;
import com.impacus.maketplace.entity.product.QProductOption;
import com.impacus.maketplace.entity.product.QShoppingBasket;
import com.impacus.maketplace.entity.product.history.QProductOptionHistory;
import com.impacus.maketplace.entity.seller.QSeller;
import com.impacus.maketplace.entity.user.QUser;
import com.impacus.maketplace.repository.payment.checkout.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Collections;
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
    public List<CheckoutProductWithDetailsByCartDTO> findCheckoutProductWithDetailsByCart(Long userId, List<Long> shoppingBasketIdList) {
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
                .where(shoppingBasket.id.in(shoppingBasketIdList).and(shoppingBasket.userId.eq(userId)))
                .fetch();
    }

    @Override
    public BuyerInfoDTO getBuyerInfo(Long userId) {
        return queryFactory
                .select(new QBuyerInfoDTO(
                        user.id,
                        user.email,
                        user.name,
                        user.phoneNumberPrefix,
                        user.phoneNumberSuffix
                ))
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();
    }

    /**
     * 추후 카드 등록 관련 로직 추가시 수정
     */
    @Override
    public CheckoutProductInfoDTO getPaymentProductInfo(Long productId, Long productOptionId, Long sellerId, Boolean usedRegisteredCard, Long registeredCardId) {
        CheckoutProductInfoDTO checkoutProductInfoDTO = queryFactory
                .select(new QCheckoutProductInfoDTO(
                        product.id,
                        seller.id,
                        seller.marketName,
                        seller.chargePercent,
                        product.name,
                        product.type,
                        product.productStatus,
                        product.appSalesPrice,
                        product.discountPrice,
                        product.salesChargePercent,
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
                .leftJoin(seller).on(seller.id.eq(sellerId).and(product.sellerId.eq(sellerId)))
                .leftJoin(productOption).on(productOption.id.eq(productOptionId).and(productOption.productId.eq(productId)))
                .leftJoin(productOptionHistory).on(productOptionHistory.productOptionId.eq(productOptionId))
                .where(product.id.eq(productId))
                .fetchOne();

        validationCheckoutProduct(checkoutProductInfoDTO);

        return checkoutProductInfoDTO;
    }

    @Override
    public List<CheckoutProductInfoDTO> getPaymentProductInfos(List<PaymentProductInfoIdDTO> paymentProductInfoIds, Boolean usedRegisteredCard, Long registeredCardId) {
        // 동적 조건 생성
        BooleanExpression combinedCondition = Expressions.FALSE;

        for (PaymentProductInfoIdDTO info : paymentProductInfoIds) {
            BooleanExpression condition = product.id.eq(info.getProductId())
                    .and(productOption.id.eq(info.getProductOptionId()))
                    .and(seller.id.eq(info.getSellerId()));

            combinedCondition = combinedCondition.or(condition);
        }

        // 쿼리 실행
        List<CheckoutProductInfoDTO> checkoutProductInfos = queryFactory
                .select(new QCheckoutProductInfoDTO(
                        product.id,
                        seller.id,
                        seller.marketName,
                        seller.chargePercent,
                        product.name,
                        product.type,
                        product.productStatus,
                        product.appSalesPrice,
                        product.discountPrice,
                        product.salesChargePercent,
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
                .leftJoin(seller).on(product.sellerId.eq(seller.id))
                .leftJoin(productOption).on(productOption.productId.eq(product.id))
                .leftJoin(productOptionHistory).on(productOptionHistory.productOptionId.eq(productOption.id))
                .where(combinedCondition)
                .fetch();

        validationCheckoutProducts(checkoutProductInfos, paymentProductInfoIds.size());

        return checkoutProductInfos;
    }
    private void validationCheckoutProduct(CheckoutProductInfoDTO checkoutProductInfoDTO) {
        List<CheckoutProductInfoDTO> checkoutProductInfos = Collections.singletonList(checkoutProductInfoDTO);
        validationCheckoutProducts(checkoutProductInfos, 1);
    }

    private void validationCheckoutProducts(List<CheckoutProductInfoDTO> checkoutProductInfos, int size) {
        if (checkoutProductInfos.size() != size) {
            throw new CustomException(PaymentErrorType.NOT_FOUND_PRODUCT);
        }

        for (CheckoutProductInfoDTO checkoutProductInfoDTO : checkoutProductInfos) {
            if (checkoutProductInfoDTO.getProductOptionId() == null) {
                throw new CustomException(PaymentErrorType.NOT_FOUND_MATCHED_PRODUCT_OPTION);
            }

            if (checkoutProductInfoDTO.getProductOptionHistoryId() == null) {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, PaymentErrorType.NOT_FOUND_MATCHED_PRODUCT_OPTION_HISTORY);
            }

            if (checkoutProductInfoDTO.getSellerId() == null) {
                throw new CustomException(PaymentErrorType.NOT_FOUND_MATCHED_SELLER);
            }
        }
    }

}

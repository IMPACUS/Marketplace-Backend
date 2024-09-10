package com.impacus.maketplace.service.payment.checkout;

import com.impacus.maketplace.common.enumType.error.OrderErrorType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.payment.request.CheckoutSingleDTO;
import com.impacus.maketplace.dto.payment.response.CheckoutProductDTO;
import com.impacus.maketplace.repository.payment.checkout.CheckoutCustomRepository;
import com.impacus.maketplace.repository.payment.checkout.dto.BuyerInfoDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.PaymentProductInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckoutService {

    private final CheckoutCustomRepository checkoutCustomRepository;

    /**
     * 단일 주문 상품 조회
     *
     * @param productId       주문 상품 id
     * @param productOptionId 주문 상품 option id
     * @param quantity        주문 수량
     */
    @Transactional
    public CheckoutProductDTO getCheckoutSingle(Long productId, Long productOptionId, Long quantity) {


        // 1. 필요한 데이터 전부 가져오기
        CheckoutProductWithDetailsDTO checkoutProductWithDeatilsDTO = checkoutCustomRepository.findCheckoutProductWithDetails(productId, productOptionId);

        // 2. 유효성 검증
        if (checkoutProductWithDeatilsDTO == null) {
            throw new CustomException(OrderErrorType.NOT_FOUND_ORDER_PRODUCT);
        }
        validateCheckoutProduct(checkoutProductWithDeatilsDTO.isProductIsDeleted(), checkoutProductWithDeatilsDTO.isOptionIsDeleted(), checkoutProductWithDeatilsDTO.getProductStatus(), checkoutProductWithDeatilsDTO.getStock(), quantity);

        // 3. 필요 데이터 DTO로 변환 후 내려주기
        return new CheckoutProductDTO(checkoutProductWithDeatilsDTO, productId, productOptionId, quantity);
    }

    /**
     * 장바구나 id List를 이용해서 주문 상품 조회
     *
     * @param shoppingBasketIdList 장바구니 id List
     */
    public List<CheckoutProductDTO> getCheckoutCart(List<Long> shoppingBasketIdList) {

        // 1. 필요한 모든 데이터 가져오기
        List<CheckoutProductWithDetailsByCartDTO> checkoutProductWithDetailsByCartDTOList = checkoutCustomRepository.findCheckoutProductWithDetailsByCart(shoppingBasketIdList);

        // 2. 유효성 검증
        if (checkoutProductWithDetailsByCartDTOList.isEmpty()) {
            throw new CustomException(OrderErrorType.NOT_FOUND_ORDER_PRODUCT);
        }
        checkoutProductWithDetailsByCartDTOList.forEach(orderProductWithDetailsByCartDTO ->
                validateCheckoutProduct(orderProductWithDetailsByCartDTO.isProductIsDeleted(),
                        orderProductWithDetailsByCartDTO.isOptionIsDeleted(),
                        orderProductWithDetailsByCartDTO.getProductStatus(),
                        orderProductWithDetailsByCartDTO.getStock(),
                        orderProductWithDetailsByCartDTO.getQuantity()));

        return checkoutProductWithDetailsByCartDTOList.stream()
                .map(CheckoutProductDTO::new)
                .toList();
    }

    /**
     * 결제 처리 준비
     */
    @Transactional
    public void checkoutSingle(Long userId, CheckoutSingleDTO checkoutSingleDTO) {
        // 1. 필요한 사용자 정보 가져오기
        BuyerInfoDTO buyerInfoDTO = checkoutCustomRepository.getBuyerInfo(userId);

        // 2. 필요한 정보 가져오기
        PaymentProductInfoDTO paymentProductInfoDTO = checkoutCustomRepository.getPaymentProductInfo(
                checkoutSingleDTO.getPaymentProductInfo().getProductId(),
                checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(),
                checkoutSingleDTO.getPaymentProductInfo().getSellerId(),
                checkoutSingleDTO.getUsedRegisteredCard(),
                checkoutSingleDTO.getRegisteredCardId()
        );

        // 3. validateCheckoutProduct
        validateCheckoutProduct(paymentProductInfoDTO.isProductIsDeleted(), paymentProductInfoDTO.isOptionIsDeleted(), paymentProductInfoDTO.getProductStatus(), paymentProductInfoDTO.getStock(), checkoutSingleDTO.getPaymentProductInfo().getQuantity());

        // 4. validateDiscount
        List<Long> usedUserCouponIds = new ArrayList<>(checkoutSingleDTO.getAppliedCommonUserCouponIds());
        usedUserCouponIds.addAll(checkoutSingleDTO.getPaymentProductInfo().getAppliedUserCouponIds());
        validateDiscount(userId, usedUserCouponIds, checkoutSingleDTO.getPointAmount());
        // 4. address, paymentEvent, paymentOrder 저장

        // 5. Response DTO 반환
    }

    private void validateDiscount(Long userId, List<Long> usedUserCouponIds, Long pointAmount) {

    }

    private void validateCheckoutProduct(boolean productIsDeleted, boolean productOptionIsDeleted, ProductStatus productStatus, Long stock, Long quantity) {

        // 1. 상품이 삭제 되었는지
        if (productIsDeleted) {
            throw new CustomException(OrderErrorType.DELETED_ORDER_PRODUCT);
        }

        // 2. 상품의 상태가 판매중인지
        if (!productStatus.equals(ProductStatus.SALES_PROGRESS)) {
            if (productStatus.equals(ProductStatus.SALES_STOP)) {
                throw new CustomException(OrderErrorType.SALE_STOP_ORDER_PRODUCT);
            }
            if (productStatus.equals(ProductStatus.SOLD_OUT)) {
                throw new CustomException(OrderErrorType.SOLD_OUT_ORDER_PRODUCT);
            }
        }

        // 3. 옵션이 삭제되었는지
        if (productOptionIsDeleted) {
            throw new CustomException(OrderErrorType.DELETED_ORDER_PRODUCT_OPTION);
        }

        // 4. 재고가 부족한지
        if (stock < quantity) {
            throw new CustomException(OrderErrorType.OUT_OF_STOCK_ORDER_PRODUCT);
        }
    }
}

package com.impacus.maketplace.service.payment.checkout;

import com.impacus.maketplace.common.enumType.error.OrderErrorType;
import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.enumType.payment.PaymentType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.common.utils.OrderUtils;
import com.impacus.maketplace.config.PaymentConfig;
import com.impacus.maketplace.dto.payment.CheckoutCartProductInfoDTO;
import com.impacus.maketplace.dto.payment.PaymentCouponDTO;
import com.impacus.maketplace.dto.payment.request.CheckoutCartDTO;
import com.impacus.maketplace.dto.payment.request.CheckoutSingleDTO;
import com.impacus.maketplace.dto.payment.response.CheckoutCustomerDTO;
import com.impacus.maketplace.dto.payment.response.CheckoutProductDTO;
import com.impacus.maketplace.dto.payment.response.PaymentCartDTO;
import com.impacus.maketplace.dto.payment.response.PaymentSingleDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelPointDTO;
import com.impacus.maketplace.entity.address.DeliveryAddress;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.address.DeliveryAddressRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.payment.checkout.CheckoutCustomRepository;
import com.impacus.maketplace.repository.payment.checkout.dto.BuyerInfoDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductInfoDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsDTO;
import com.impacus.maketplace.service.coupon.CouponUserService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckoutService {

    private final CheckoutCustomRepository checkoutCustomRepository;
    private final CouponUserService couponUserService;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;

    private final PaymentConfig paymentConfig;

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
     * 결제 처리 준비 (단일 상품 구매)
     * Refactoring:
     * 1. 쿠폰 검증 로직 수정(전체 쿠폰 가져온 뒤 쿠폰 서비스 이용)
     */
    @Transactional
    public PaymentSingleDTO checkoutSingle(Long userId, CheckoutSingleDTO checkoutSingleDTO) {
        // 1. 중복 쿠폰 사용 체크
        List<Long> allCouponIds = new ArrayList<>();
        allCouponIds.addAll(checkoutSingleDTO.getPaymentProductInfo().getAppliedCouponForProductIds());
        allCouponIds.addAll(checkoutSingleDTO.getAppliedCommonUserCouponIds());

        validateDuplicatedCoupon(allCouponIds);

        // 2. 필요한 사용자 정보 가져오기
        BuyerInfoDTO buyerInfoDTO = checkoutCustomRepository.getBuyerInfo(userId);

        // 3. 필요한 정보 가져오기
        CheckoutProductInfoDTO checkoutProductInfoDTO = checkoutCustomRepository.getPaymentProductInfo(
                checkoutSingleDTO.getPaymentProductInfo().getProductId(),
                checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(),
                checkoutSingleDTO.getPaymentProductInfo().getSellerId(),
                checkoutSingleDTO.getUsedRegisteredCard(),
                checkoutSingleDTO.getRegisteredCardId()
        );

        // 4. validateCheckoutProduct
        validateCheckoutProduct(checkoutProductInfoDTO.isProductIsDeleted(), checkoutProductInfoDTO.isOptionIsDeleted(), checkoutProductInfoDTO.getProductStatus(), checkoutProductInfoDTO.getStock(), checkoutSingleDTO.getPaymentProductInfo().getQuantity());

        // 5. validateDiscount
        List<PaymentCouponDTO> paymentCouponsForProduct = couponUserService.getAmountAfterValidateCouponsForProduct(userId, checkoutSingleDTO.getPaymentProductInfo().getAppliedCouponForProductIds(), checkoutProductInfoDTO.getProductType(), checkoutProductInfoDTO.getMarketName(), checkoutProductInfoDTO.getAppSalesPrice(), checkoutSingleDTO.getPaymentProductInfo().getQuantity());
        Long totalPrice = checkoutProductInfoDTO.getAppSalesPrice() * checkoutSingleDTO.getPaymentProductInfo().getQuantity();
        List<PaymentCouponDTO> paymentCouponsForOrder = couponUserService.getAmountAfterValidateCouponsForOrder(userId, checkoutSingleDTO.getAppliedCommonUserCouponIds(), totalPrice);

        GreenLabelPointDTO greenLabelPointInformation = greenLabelPointAllocationService.getGreenLabelPointInformation(userId);
        if (greenLabelPointInformation.getGreenLabelPoint() < checkoutSingleDTO.getPointAmount()) {
            throw new CustomException(PaymentErrorType.NOT_ENOUGH_POINT_AMOUNT);
        }

        // 6. order_id 및 payment_id 생성
        String orderId = getOrderId();
        String paymentKey;
        try {
            paymentKey = OrderUtils.generatePaymentKey(checkoutSingleDTO);
        } catch (Exception e) {
            LogUtils.error(this.getClass().toString(), "Fail to generate payment id caused over threshold count", e);
            throw new CustomException(PaymentErrorType.FAIL_GENERATE_PAYMENT_ID);
        }

        String orderName = OrderUtils.generateOrderName(checkoutProductInfoDTO.getName(), checkoutSingleDTO.getPaymentProductInfo().getQuantity(), 1);

        // 7. PaymentEvent, PaymentOrder, DeliveyAddress 저장
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .buyerId(userId)
                .isPaymentDone(false)
                .paymentKey(paymentKey)
                .orderId(orderId)
                .type(PaymentType.NORMAL)   // 추후 수정
                .orderName(orderName)
                .method(checkoutSingleDTO.getMethod())
                .build();

        paymentEventRepository.save(paymentEvent);

        PaymentOrder paymentOrder = PaymentOrder.builder()
                .paymentEventId(paymentEvent.getId())
                .sellerId(checkoutProductInfoDTO.getSellerId())
                .productId(checkoutProductInfoDTO.getProductId())
                .productOptionHistoryId(checkoutProductInfoDTO.getProductOptionHistoryId())
                .quantity(checkoutSingleDTO.getPaymentProductInfo().getQuantity())
                .orderId(orderId)
                .amount((long) checkoutProductInfoDTO.getAppSalesPrice())
                .ecoDiscount((long) (checkoutProductInfoDTO.getAppSalesPrice() - checkoutProductInfoDTO.getDiscountPrice()))
                .greenLabelDiscount(checkoutSingleDTO.getPointAmount())
                //.couponDiscount(amountCouponDiscountForProduct + amountCouponDistcountForOrder)
                .commissionPercent(checkoutProductInfoDTO.getChargePercent())
                .status(PaymentOrderStatus.NOT_STARTED)
                .ledgerUpdated(false)
                .walletUpdated(false)
                .isPaymentDone(false)
                .failedCount(0)
                .threshold(5)
                .build();

        paymentEvent.getPaymentOrders().add(paymentOrder);

        paymentOrderRepository.save(paymentOrder);

        DeliveryAddress deliveryAddress = checkoutSingleDTO.getAddressInfoDTO().toEntity(paymentEvent.getId());

        deliveryAddressRepository.save(deliveryAddress);

        // 7.1 PaymentEventCoupon 및 PaymentOrderCoupon에 등록 후 사용 처리를 false로 처리

        // 8. Response DTO 반환
        CheckoutCustomerDTO checkoutCustomerDTO = new CheckoutCustomerDTO(
                buyerInfoDTO.getUserId(), buyerInfoDTO.getName(), buyerInfoDTO.getPhoneNumber(), buyerInfoDTO.getEmail(),
                checkoutSingleDTO.getAddressInfoDTO().getAddress(), checkoutSingleDTO.getAddressInfoDTO().getDetailAddress(), checkoutSingleDTO.getAddressInfoDTO().getPostalCode()
        );

        return PaymentSingleDTO.builder()
                .storeId(paymentConfig.getStoreId())
                .paymentKey(orderId)
                .orderName(orderName)
                .totalDiscountedAmount(paymentEvent.getTotalDiscountedAmount())
                .currency("KRW")
                .channelKey(paymentConfig.getChannelKeyByPaymentMethod(paymentEvent.getMethod()))
                .paymentMethod(paymentEvent.getMethod())
                .customer(checkoutCustomerDTO)
                .build();
    }

    /**
     * 결제 처리 준비 (장바구니 상품 구매)
     * Refactoring:
     * 1. 쿠폰 검증 로직 수정(전체 쿠폰 가져온 뒤 쿠폰 서비스 이용)
     */
    @Transactional
    public PaymentCartDTO checkoutCart(Long userId, CheckoutCartDTO checkoutCartDTO) {

        // 1. 중복 쿠폰 사용 체크
        List<Long> allCouponIds = new ArrayList<>();
        allCouponIds.addAll(checkoutCartDTO.getAppliedCommonUserCouponIds());
        allCouponIds.addAll(
                checkoutCartDTO.getPaymentProductInfos().stream()
                        .flatMap(paymentProductInfoDTO -> paymentProductInfoDTO.getAppliedCouponForProductIds().stream())
                        .toList()
        );

        validateDuplicatedCoupon(allCouponIds);

        // 2. 필요한 사용자 정보 가져오기
        BuyerInfoDTO buyerInfo = checkoutCustomRepository.getBuyerInfo(userId);

        // 3. 필요한 정보 가져오기
        List<CheckoutCartProductInfoDTO> checkoutCartProductInfoDTOList = checkoutCartDTO.getPaymentProductInfos().stream().map(paymentProductInfoDTO -> {
                    CheckoutProductInfoDTO checkoutProductInfoDTO = checkoutCustomRepository.getPaymentProductInfo(paymentProductInfoDTO.getProductId(), paymentProductInfoDTO.getProductOptionId(), paymentProductInfoDTO.getSellerId(), checkoutCartDTO.getUsedRegisteredCard(), checkoutCartDTO.getRegisteredCardId());
                    return CheckoutCartProductInfoDTO.builder()
                            .checkoutProductInfoDTO(checkoutProductInfoDTO)
                            .quantity(paymentProductInfoDTO.getQuantity())
                            .appliedCouponForProductIds(paymentProductInfoDTO.getAppliedCouponForProductIds())
                            .build();
                }
        ).toList();

        Integer size = checkoutCartProductInfoDTOList.size();

        // 4. validateCheckoutCartProduct
        checkoutCartProductInfoDTOList.forEach(checkoutCartProductInfoDTO ->
                validateCheckoutProduct(checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().isProductIsDeleted(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().isOptionIsDeleted(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getProductStatus(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getStock(), checkoutCartProductInfoDTO.getQuantity())
        );

        // 5. validateDiscount
//        Long amountCouponDiscountForProduct = checkoutCartProductInfoDTOList.stream().mapToLong(checkoutCartProductInfoDTO ->
//                couponUserService.getAmountAfterValidateCouponsForProduct(userId, checkoutCartProductInfoDTO.getAppliedCouponForProductIds(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getProductType(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getMarketName(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getAppSalesPrice(), checkoutCartProductInfoDTO.getQuantity())
//        ).sum();
        Long totalPrice = checkoutCartProductInfoDTOList.stream().mapToLong(chekcoutCartProductInfoDTO ->
                chekcoutCartProductInfoDTO.getCheckoutProductInfoDTO().getAppSalesPrice() * chekcoutCartProductInfoDTO.getQuantity()
        ).sum();
//        Long amountCouponDistcountForOrder = couponUserService.getAmountAfterValidateCouponsForOrder(userId, checkoutCartDTO.getAppliedCommonUserCouponIds(), totalPrice);

        // 6. order_id 및 payment_id 생성
        String orderId = getOrderId();
        String paymentKey;
        try {
            paymentKey = OrderUtils.generatePaymentKey(checkoutCartDTO);
        } catch (Exception e) {
            LogUtils.error(this.getClass().toString(), "Fail to generate payment id caused over threshold count", e);
            throw new CustomException(PaymentErrorType.FAIL_GENERATE_PAYMENT_ID);
        }

        String orderName = OrderUtils.generateOrderName(checkoutCartProductInfoDTOList.get(0).getCheckoutProductInfoDTO().getName(), checkoutCartProductInfoDTOList.get(0).getQuantity(), size);

        // 7. PaymentEvent, PaymentOrder, DeliveyAddress 저장
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .buyerId(userId)
                .isPaymentDone(false)
                .paymentKey(paymentKey)
                .orderId(orderId)
                .type(PaymentType.NORMAL)   // 추후 수정
                .orderName(orderName)
                .method(checkoutCartDTO.getMethod())
                .build();

        paymentEventRepository.save(paymentEvent);


        // 8. Response DTO 반환

        return null;
    }

    private static void validateDuplicatedCoupon(List<Long> allCouponIds) {
        Set<Long> uniqueCouponIds = new HashSet<>();

        for (Long id : allCouponIds) {
            if (!uniqueCouponIds.add(id)) {
                throw new CustomException(PaymentErrorType.DUPLICATE_USE_USER_COUPON);
            }
        }
    }

    /**
     * 주문 id 생성
     * required: 재시도 로직 수정 필요
     */
    private String getOrderId() {

        int count = 1;
        String orderId = OrderUtils.generateOrderNumber();

        while (paymentEventRepository.existsByOrderId(orderId)) {
            if (count == 5) {
                LogUtils.writeInfoLog(this.getClass().toString(), "Fail to generate order id caused over threshold count");
                throw new CustomException(PaymentErrorType.FAIL_GENERATE_ORDER_ID);
            }
            orderId = OrderUtils.generateOrderNumber();
            count++;
        }

        return orderId;
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

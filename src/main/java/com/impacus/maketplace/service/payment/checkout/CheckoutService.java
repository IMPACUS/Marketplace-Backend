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
import com.impacus.maketplace.dto.payment.DiscountInfoDTO;
import com.impacus.maketplace.dto.payment.PaymentCouponDTO;
import com.impacus.maketplace.dto.payment.ProductPricingDTO;
import com.impacus.maketplace.dto.payment.request.CheckoutCartDTO;
import com.impacus.maketplace.dto.payment.request.CheckoutSingleDTO;
import com.impacus.maketplace.dto.payment.response.CheckoutCustomerDTO;
import com.impacus.maketplace.dto.payment.response.CheckoutProductDTO;
import com.impacus.maketplace.dto.payment.response.PaymentCartDTO;
import com.impacus.maketplace.dto.payment.response.PaymentSingleDTO;
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
import com.impacus.maketplace.service.coupon.CouponRedeemService;
import com.impacus.maketplace.service.payment.DiscountService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckoutService {

    private final CheckoutCustomRepository checkoutCustomRepository;
    private final CouponRedeemService couponRedeemService;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final DiscountService discountService;

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
        if (checkoutSingleDTO.getPaymentProductInfo().getAppliedCouponForProductIds() != null) {
            allCouponIds.addAll(checkoutSingleDTO.getPaymentProductInfo().getAppliedCouponForProductIds());
        }
        if (checkoutSingleDTO.getAppliedCommonUserCouponIds() != null) {
            allCouponIds.addAll(checkoutSingleDTO.getAppliedCommonUserCouponIds());
        }

        validateDuplicatedCoupon(allCouponIds);

        // 2. 필요한 사용자 정보 가져오기
        BuyerInfoDTO buyerInfo = checkoutCustomRepository.getBuyerInfo(userId);

        // 3. 필요한 정보 가져오기
        CheckoutProductInfoDTO checkoutProductInfo = checkoutCustomRepository.getPaymentProductInfo(
                checkoutSingleDTO.getPaymentProductInfo().getProductId(),
                checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(),
                checkoutSingleDTO.getPaymentProductInfo().getSellerId(),
                checkoutSingleDTO.getUsedRegisteredCard(),
                checkoutSingleDTO.getRegisteredCardId()
        );

        // 4. validateCheckoutProduct
        validateCheckoutProduct(checkoutProductInfo.isProductIsDeleted(), checkoutProductInfo.isOptionIsDeleted(), checkoutProductInfo.getProductStatus(), checkoutProductInfo.getStock(), checkoutSingleDTO.getPaymentProductInfo().getQuantity());

        // 5. validateDiscount
        Long greenLabelPoint = greenLabelPointAllocationService.getGreenLabelPointAmount(userId);
        if (greenLabelPoint < checkoutSingleDTO.getPointAmount()) {
            throw new CustomException(PaymentErrorType.NOT_ENOUGH_POINT_AMOUNT);
        }

        List<PaymentCouponDTO> productCoupons = couponRedeemService.getPaymentCouponForProductAfterValidation(userId, checkoutSingleDTO.getPaymentProductInfo().getAppliedCouponForProductIds(), checkoutProductInfo.getProductType(), checkoutProductInfo.getMarketName(), checkoutProductInfo.getAppSalesPrice(), checkoutSingleDTO.getPaymentProductInfo().getQuantity());
        Long totalPrice = checkoutProductInfo.getAppSalesPrice() * checkoutSingleDTO.getPaymentProductInfo().getQuantity();
        List<PaymentCouponDTO> orderCoupons = couponRedeemService.getPaymentCouponForOrderAfterValidation(userId, checkoutSingleDTO.getAppliedCommonUserCouponIds(), totalPrice);

        Long productCouponDiscount = discountService.calculateProductCouponDiscount(checkoutProductInfo.getProductId(), totalPrice, productCoupons);
        Long orderCouponDiscount = discountService.calculateOrderCouponDiscount(checkoutProductInfo.getProductId(), totalPrice, orderCoupons);
        Long pointDiscount = discountService.calculatePointDiscount(checkoutProductInfo.getProductId(), totalPrice, checkoutSingleDTO.getPointAmount());

        ProductPricingDTO productPricing = ProductPricingDTO.builder()
                .productId(checkoutProductInfo.getProductId())
                .appSalesPrice((long) checkoutProductInfo.getAppSalesPrice())
                .ecoDiscountAmount(checkoutProductInfo.getEcoDiscount())
                .quantity(checkoutSingleDTO.getPaymentProductInfo().getQuantity())
                .build();

        DiscountInfoDTO discountInfo = discountService.reconcileDiscountAmount(productPricing, productCouponDiscount, orderCouponDiscount, pointDiscount);

        // 6. 최종 결제 금액 비교
        if (!discountInfo.getFinalAmount().equals(checkoutSingleDTO.getCalculatedTotalAmount())) {
            throw new CustomException(PaymentErrorType.MISMATCH_TOTAL_AMOUNT);
        }

        // 7. order_id 및 payment_id 생성
        String orderId = getOrderId();
        String paymentKey;
        try {
            paymentKey = OrderUtils.generatePaymentKey(checkoutSingleDTO);
        } catch (Exception e) {
            LogUtils.error(this.getClass().toString(), "Fail to generate payment id caused over threshold count", e);
            throw new CustomException(PaymentErrorType.FAIL_GENERATE_PAYMENT_ID);
        }

        String orderName = OrderUtils.generateOrderName(checkoutProductInfo.getName(), checkoutSingleDTO.getPaymentProductInfo().getQuantity(), 1);

        // 8. PaymentEvent, PaymentOrder, DeliveyAddress 저장
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .buyerId(userId)
                .isPaymentDone(false)
                .paymentKey(paymentKey)
                .orderId(orderId)
                .type(PaymentType.NORMAL)   // 추후 수정
                .orderName(orderName)
                .method(checkoutSingleDTO.getMethod())
                .build();

        PaymentEvent savedPaymentEvent = paymentEventRepository.save(paymentEvent);

        PaymentOrder paymentOrder = PaymentOrder.builder()
                .paymentEventId(savedPaymentEvent.getId())
                .sellerId(checkoutProductInfo.getSellerId())
                .productId(checkoutProductInfo.getProductId())
                .productOptionHistoryId(checkoutProductInfo.getProductOptionHistoryId())
                .quantity(checkoutSingleDTO.getPaymentProductInfo().getQuantity())
                .orderId(orderId)
                .amount((long) checkoutProductInfo.getAppSalesPrice())
                .ecoDiscount(discountInfo.getEcoDiscountAmount())
                .greenLabelDiscount(discountInfo.getPointDiscountAmount())
                .couponDiscount(discountInfo.getFinalCouponDiscount())
                .commissionPercent(checkoutProductInfo.getChargePercent())
                .status(PaymentOrderStatus.NOT_STARTED)
                .ledgerUpdated(false)
                .walletUpdated(false)
                .isPaymentDone(false)
                .failedCount(0)
                .threshold(5)
                .build();

        savedPaymentEvent.getPaymentOrders().add(paymentOrder);

        PaymentOrder savedPaymentOrder = paymentOrderRepository.save(paymentOrder);

        DeliveryAddress deliveryAddress = checkoutSingleDTO.getAddressInfoDTO().toEntity(savedPaymentEvent.getId());

        deliveryAddressRepository.save(deliveryAddress);

        // 9. PaymentEventCoupon 및 PaymentOrderCoupon에 등록 후 사용 처리를 false로 처리
        List<Long> paymentOrderCouponIds = orderCoupons.stream().map(PaymentCouponDTO::getUserCouponId).toList();
        couponRedeemService.registPaymentOrderCoupons(savedPaymentOrder.getId(), paymentOrderCouponIds);

        List<Long> paymentEventCouponIds = productCoupons.stream().map(PaymentCouponDTO::getUserCouponId).toList();
        couponRedeemService.registPaymentEventCoupons(savedPaymentEvent.getId(), paymentEventCouponIds);

        // 10. Response DTO 반환
        CheckoutCustomerDTO checkoutCustomerDTO = new CheckoutCustomerDTO(
                buyerInfo.getUserId(), buyerInfo.getName(), buyerInfo.getPhoneNumber(), buyerInfo.getEmail(),
                checkoutSingleDTO.getAddressInfoDTO().getAddress(), checkoutSingleDTO.getAddressInfoDTO().getDetailAddress(), checkoutSingleDTO.getAddressInfoDTO().getPostalCode()
        );

        return PaymentSingleDTO.builder()
                .storeId(paymentConfig.getStoreId())
                .paymentKey(orderId)
                .orderName(orderName)
                .totalDiscountedAmount(savedPaymentEvent.getTotalDiscountedAmount())
                .currency("KRW")
                .channelKey(paymentConfig.getChannelKeyByPaymentMethod(savedPaymentEvent.getMethod()))
                .paymentMethod(savedPaymentEvent.getMethod())
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
        List<CheckoutCartProductInfoDTO> checkoutCartProductList = checkoutCartDTO.getPaymentProductInfos().stream().map(paymentProductInfoDTO -> {
                    CheckoutProductInfoDTO checkoutProductInfoDTO = checkoutCustomRepository.getPaymentProductInfo(paymentProductInfoDTO.getProductId(), paymentProductInfoDTO.getProductOptionId(), paymentProductInfoDTO.getSellerId(), checkoutCartDTO.getUsedRegisteredCard(), checkoutCartDTO.getRegisteredCardId());
                    return CheckoutCartProductInfoDTO.builder()
                            .checkoutProductInfoDTO(checkoutProductInfoDTO)
                            .quantity(paymentProductInfoDTO.getQuantity())
                            .appliedCouponForProductIds(paymentProductInfoDTO.getAppliedCouponForProductIds())
                            .build();
                }
        ).toList();

        Integer size = checkoutCartProductList.size();

        // 4. validateCheckoutCartProduct
        checkoutCartProductList.forEach(checkoutCartProductInfoDTO ->
                validateCheckoutProduct(checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().isProductIsDeleted(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().isOptionIsDeleted(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getProductStatus(), checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getStock(), checkoutCartProductInfoDTO.getQuantity())
        );

        // 5. validateDiscount
        Long greenLabelPoint = greenLabelPointAllocationService.getGreenLabelPointAmount(userId);
        if (greenLabelPoint < checkoutCartDTO.getPointAmount()) {
            throw new CustomException(PaymentErrorType.NOT_ENOUGH_POINT_AMOUNT);
        }

        Map<Long, List<PaymentCouponDTO>> productCoupons = checkoutCartProductList.stream()
                .collect(Collectors.toMap(
                        item -> item.getCheckoutProductInfoDTO().getProductId(),
                        item -> couponRedeemService.getPaymentCouponForProductAfterValidation(userId, item.getAppliedCouponForProductIds(), item.getCheckoutProductInfoDTO().getProductType(), item.getCheckoutProductInfoDTO().getMarketName(), item.getCheckoutProductInfoDTO().getAppSalesPrice(), item.getQuantity())
                ));
        Long totalPrice = checkoutCartProductList.stream().mapToLong(chekcoutCartProductInfoDTO ->
                chekcoutCartProductInfoDTO.getCheckoutProductInfoDTO().getAppSalesPrice() * chekcoutCartProductInfoDTO.getQuantity()
        ).sum();

        List<PaymentCouponDTO> orderCoupons = couponRedeemService.getPaymentCouponForOrderAfterValidation(userId, checkoutCartDTO.getAppliedCommonUserCouponIds(), totalPrice);

        Map<Long, Long> productPrices = checkoutCartProductList.stream()
                .collect(Collectors.toMap(
                        item -> item.getCheckoutProductInfoDTO().getProductId(),
                        item -> (long) item.getCheckoutProductInfoDTO().getAppSalesPrice() * item.getQuantity()
                ));

        Map<Long, Long> productCouponDiscounts = discountService.calculateProductCouponDiscounts(productPrices, productCoupons);
        Map<Long, Long> orderCouponDiscounts = discountService.calculateOrderCouponDiscounts(totalPrice, productPrices, orderCoupons);
        Map<Long, Long> pointDiscounts = discountService.calculatePointDiscounts(totalPrice, productPrices, checkoutCartDTO.getPointAmount());

        Map<Long, ProductPricingDTO> productPricings = checkoutCartProductList.stream()
                .collect(Collectors.toMap(
                        item -> item.getCheckoutProductInfoDTO().getProductId(),
                        item -> ProductPricingDTO.builder()
                                .productId(item.getCheckoutProductInfoDTO().getProductId())
                                .appSalesPrice((long) item.getCheckoutProductInfoDTO().getAppSalesPrice())
                                .ecoDiscountAmount(item.getCheckoutProductInfoDTO().getEcoDiscount())
                                .quantity(item.getQuantity())
                                .build()
                ));

        Map<Long, DiscountInfoDTO> discountInfos = discountService.reconcileDiscountAmounts(productPricings, productCouponDiscounts, orderCouponDiscounts, pointDiscounts);

        Long totalDiscountedAmount = discountInfos.values().stream().mapToLong(DiscountInfoDTO::getFinalAmount)
                .sum();

        // 6. 최종 결제 금액 비교
        if (!totalDiscountedAmount.equals(checkoutCartDTO.getCalculatedTotalAmount())) {
            throw new CustomException(PaymentErrorType.MISMATCH_TOTAL_AMOUNT);
        }

        // 7. order_id 및 payment_id 생성
        String orderId = getOrderId();
        String paymentKey;
        try {
            paymentKey = OrderUtils.generatePaymentKey(checkoutCartDTO);
        } catch (Exception e) {
            LogUtils.error(this.getClass().toString(), "Fail to generate payment id caused over threshold count", e);
            throw new CustomException(PaymentErrorType.FAIL_GENERATE_PAYMENT_ID);
        }

        String orderName = OrderUtils.generateOrderName(checkoutCartProductList.get(0).getCheckoutProductInfoDTO().getName(), checkoutCartProductList.get(0).getQuantity(), size);

        // 8. PaymentEvent, PaymentOrder, DeliveyAddress 저장
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .buyerId(userId)
                .isPaymentDone(false)
                .paymentKey(paymentKey)
                .orderId(orderId)
                .type(PaymentType.NORMAL)   // 추후 수정
                .orderName(orderName)
                .method(checkoutCartDTO.getMethod())
                .build();

        PaymentEvent savedPaymentEvent = paymentEventRepository.save(paymentEvent); // 새로운 변수에 저장

        List<PaymentOrder> paymentOrders = checkoutCartProductList.stream().map(checkoutCartProductInfoDTO -> {
                            DiscountInfoDTO discountInfo = discountInfos.get(checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getProductId());
                            return PaymentOrder.builder()
                                    .paymentEventId(savedPaymentEvent.getId())
                                    .sellerId(checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getSellerId())
                                    .productId(checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getProductId())
                                    .productOptionHistoryId(checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getProductOptionHistoryId())
                                    .quantity(checkoutCartProductInfoDTO.getQuantity())
                                    .orderId(orderId)
                                    .amount((long) checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getAppSalesPrice())
                                    .ecoDiscount(discountInfo.getEcoDiscountAmount())
                                    .greenLabelDiscount(discountInfo.getPointDiscountAmount())
                                    .couponDiscount(discountInfo.getFinalCouponDiscount())
                                    .commissionPercent(checkoutCartProductInfoDTO.getCheckoutProductInfoDTO().getChargePercent())
                                    .status(PaymentOrderStatus.NOT_STARTED)
                                    .ledgerUpdated(false)
                                    .walletUpdated(false)
                                    .isPaymentDone(false)
                                    .failedCount(0)
                                    .threshold(5)
                                    .build();
                        }
                )
                .toList();

        savedPaymentEvent.getPaymentOrders().addAll(paymentOrders);

        List<PaymentOrder> savedPaymentOrders = paymentOrderRepository.saveAll(paymentOrders);

        DeliveryAddress deliveryAddress = checkoutCartDTO.getAddressInfoDTO().toEntity(savedPaymentEvent.getId());

        deliveryAddressRepository.save(deliveryAddress);

        // 9. PaymentEventCoupon 및 PaymentOrderCoupon에 등록 후 사용 처리를 false로 처리
        savedPaymentOrders.forEach(paymentOrder -> {
            List<PaymentCouponDTO> paymentCoupons = productCoupons.getOrDefault(paymentOrder.getProductId(), null);
            List<Long> userCouponIds = paymentCoupons.stream().map(PaymentCouponDTO::getUserCouponId).toList();
            couponRedeemService.registPaymentOrderCoupons(paymentOrder.getPaymentEventId(), userCouponIds);
        });

        List<Long> paymentOrderCouponIds = orderCoupons.stream().map(PaymentCouponDTO::getUserCouponId).toList();
        couponRedeemService.registPaymentEventCoupons(savedPaymentEvent.getId(), paymentOrderCouponIds);

        // 10. Response DTO 반환
        CheckoutCustomerDTO checkoutCustomerDTO = new CheckoutCustomerDTO(
                buyerInfo.getUserId(), buyerInfo.getName(), buyerInfo.getPhoneNumber(), buyerInfo.getEmail(),
                checkoutCartDTO.getAddressInfoDTO().getAddress(), checkoutCartDTO.getAddressInfoDTO().getDetailAddress(), checkoutCartDTO.getAddressInfoDTO().getPostalCode()
        );

        return PaymentCartDTO.builder()
                .storeId(paymentConfig.getStoreId())
                .paymentKey(orderId)
                .shoppingBasketIdList(checkoutCartDTO.getShoppingBasketIdList())
                .orderName(orderName)
                .totalDiscountedAmount(savedPaymentEvent.getTotalDiscountedAmount())
                .currency("KRW")
                .channelKey(paymentConfig.getChannelKeyByPaymentMethod(savedPaymentEvent.getMethod()))
                .paymentMethod(savedPaymentEvent.getMethod())
                .customer(checkoutCustomerDTO)
                .build();
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

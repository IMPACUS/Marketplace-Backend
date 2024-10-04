package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.coupon.BenefitType;
import com.impacus.maketplace.common.enumType.error.OrderErrorType;
import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.enumType.payment.PaymentType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.OrderUtils;
import com.impacus.maketplace.config.PaymentConfig;
import com.impacus.maketplace.dto.payment.DiscountInfoDTO;
import com.impacus.maketplace.dto.payment.PaymentCouponDTO;
import com.impacus.maketplace.dto.payment.ProductPricingDTO;
import com.impacus.maketplace.dto.payment.request.AddressInfoDTO;
import com.impacus.maketplace.dto.payment.request.CheckoutSingleDTO;
import com.impacus.maketplace.dto.payment.request.PaymentProductInfoDTO;
import com.impacus.maketplace.dto.payment.response.PaymentSingleDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.AppGreenLabelPointDTO;
import com.impacus.maketplace.entity.address.DeliveryAddress;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.address.DeliveryAddressRepository;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.payment.checkout.CheckoutCustomRepository;
import com.impacus.maketplace.repository.payment.checkout.dto.BuyerInfoDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductInfoDTO;
import com.impacus.maketplace.service.coupon.CouponRedeemService;
import com.impacus.maketplace.service.payment.checkout.CheckoutService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[비즈니스 로직] - 결제 체크 아웃 서비스")
public class CheckoutServiceTest {

    private final String MOCKED_ORDER_ID = "mockedOrderId";
    private final String MOCKED_ORDER_NAME = "mockedOrderName";
    private final String MOCKED_PAYMENT_KEY = "mockedPaymentKey";

    @InjectMocks
    private CheckoutService checkoutService;
    @Mock
    private CheckoutCustomRepository checkoutCustomRepository;
    @Mock
    private CouponRedeemService couponRedeemService;
    @Mock
    private GreenLabelPointAllocationService greenLabelPointAllocationService;
    @Mock
    private PaymentEventRepository paymentEventRepository;
    @Mock
    private PaymentOrderRepository paymentOrderRepository;
    @Mock
    private DeliveryAddressRepository deliveryAddressRepository;
    @Mock
    private DiscountService discountService;
    private PaymentConfig paymentConfig;

    @BeforeEach
    void setUp() {
        // PaymentConfig 객체 수동 초기화
        paymentConfig = new PaymentConfig();
        paymentConfig.setStoreId("StoreId");

        PaymentConfig.Smartro smartro = new PaymentConfig.Smartro();
        smartro.setChannelKey("SmartroChannelKey");
        smartro.setKey("SmartroKey");
        smartro.setCancelPassword("SmartroCancelPassword");
        smartro.setApiKey("SmartroApiKey");
        paymentConfig.setSmartro(smartro);

        PaymentConfig.KakaoPay kakaoPay = new PaymentConfig.KakaoPay();
        kakaoPay.setChannelKey("KakaoPayChannelKey");
        paymentConfig.setKakaoPay(kakaoPay);

        // CheckoutService를 수동으로 생성하여 모든 의존성 주입
        checkoutService = new CheckoutService(
                checkoutCustomRepository,
                couponRedeemService,
                greenLabelPointAllocationService,
                paymentEventRepository,
                paymentOrderRepository,
                deliveryAddressRepository,
                discountService,
                paymentConfig
        );
    }

    @Test
    @DisplayName("[정상 케이스] 아무런 할인 없이 단일 상품 1개 구매시 결제 초기 단계에서 처리 준비가 올바르게 동작하다.")
    void checkoutSingleOneQuantityWithNoDiscountCARD_success() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        Long pointAmount = 0L;
        int appSalesPrice = 10000;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;
        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice);

        BuyerInfoDTO buyerInfoDTO = getBuyerInfoDTO(userId);
        CheckoutProductInfoDTO checkoutProductInfoDTO = getCheckoutProductInfoDTO(checkoutSingleDTO.getPaymentProductInfo().getProductId(), appSalesPrice, discountPrice, checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId()
                ,ProductStatus.SALES_PROGRESS, false, false, 100L, ProductType.GENERAL);
        List<PaymentCouponDTO> paymentCouponsForProduct = new ArrayList<>();
        List<PaymentCouponDTO> paymentCouponsForOrder = new ArrayList<>();

        Long totalPrice = checkoutProductInfoDTO.getAppSalesPrice() * checkoutSingleDTO.getPaymentProductInfo().getQuantity();
        Long ecoDiscount = (long) checkoutProductInfoDTO.getAppSalesPrice() - checkoutProductInfoDTO.getDiscountPrice();
        Long discountProductCoupon = 0L;
        Long discountOrderCoupon = 0L;
        Long couponDiscount = discountProductCoupon + discountOrderCoupon;
        Long discountPoint = 0L;
        DiscountInfoDTO discountInfo = DiscountInfoDTO.builder()
                .appSalesPrice((long) appSalesPrice)
                .ecoDiscountAmount(0L)
                .productCouponDiscountAmount(0L)
                .orderCouponDiscountAmount(0L)
                .pointDiscountAmount(0L)
                .build();

        try (MockedStatic<OrderUtils> orderUtilsMockedStatic = Mockito.mockStatic(OrderUtils.class)) {
            orderUtilsMockedStatic.when(OrderUtils::generateOrderNumber)
                    .thenReturn("mockedOrderId");

            orderUtilsMockedStatic.when(() -> OrderUtils.generateOrderName(any(), any(), any()))
                    .thenReturn("mockedOrderName");

            orderUtilsMockedStatic.when(() -> OrderUtils.generatePaymentKey(any()))
                    .thenReturn("mockedPaymentKey");
        } catch (Exception e) {
            System.out.println(e);
        }

        PaymentEvent paymentEvent = getPaymentEvent(userId, method);
        PaymentOrder paymentOrder = getPaymentOrder(paymentEvent.getId(), checkoutProductInfoDTO.getSellerId(), checkoutProductInfoDTO.getProductId(), checkoutProductInfoDTO.getProductOptionHistoryId(), checkoutSingleDTO.getPaymentProductInfo().getQuantity(), (long) checkoutProductInfoDTO.getAppSalesPrice(), ecoDiscount, discountPoint, couponDiscount, 10);
        DeliveryAddress deliveryAddress = checkoutSingleDTO.getAddressInfoDTO().toEntity(paymentEvent.getId());

        when(checkoutCustomRepository.getBuyerInfo(userId)).thenReturn(buyerInfoDTO);
        when(checkoutCustomRepository.getPaymentProductInfo(checkoutSingleDTO.getPaymentProductInfo().getProductId(), checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId(), checkoutSingleDTO.getUsedRegisteredCard(), checkoutSingleDTO.getRegisteredCardId())).thenReturn(checkoutProductInfoDTO);
        when(couponRedeemService.getAmountAfterValidateCouponsForProduct(userId, checkoutSingleDTO.getPaymentProductInfo().getAppliedCouponForProductIds(), checkoutProductInfoDTO.getProductType(), checkoutProductInfoDTO.getMarketName(), checkoutProductInfoDTO.getAppSalesPrice(), checkoutSingleDTO.getPaymentProductInfo().getQuantity())).thenReturn(paymentCouponsForProduct);
        when(couponRedeemService.getAmountAfterValidateCouponsForOrder(userId, checkoutSingleDTO.getAppliedCommonUserCouponIds(), totalPrice)).thenReturn(paymentCouponsForOrder);
        when(greenLabelPointAllocationService.getGreenLabelPointAmount(userId)).thenReturn(0L);
        when(discountService.calculateProductCouponDiscount(checkoutProductInfoDTO.getProductId(), totalPrice, paymentCouponsForProduct)).thenReturn(0L);
        when(discountService.calculateOrderCouponDiscount(checkoutProductInfoDTO.getProductId(), totalPrice, paymentCouponsForOrder)).thenReturn(0L);
        when(discountService.calculatePointDiscount(checkoutProductInfoDTO.getProductId(), totalPrice, checkoutSingleDTO.getPointAmount())).thenReturn(0L);
        when(discountService.reconcileDiscountAmount(any(ProductPricingDTO.class), any(Long.class), any(Long.class), any(Long.class))).thenReturn(discountInfo);
        when(paymentEventRepository.save(any(PaymentEvent.class))).thenReturn(paymentEvent);
        when(paymentOrderRepository.save(any(PaymentOrder.class))).thenReturn(paymentOrder);
        when(deliveryAddressRepository.save(any(DeliveryAddress.class))).thenReturn(deliveryAddress);
        doNothing().when(couponRedeemService).registPaymentEventCoupons(any(Long.class), any(List.class));
        doNothing().when(couponRedeemService).registPaymentOrderCoupons(any(Long.class), any(List.class));

        // when
        PaymentSingleDTO result = checkoutService.checkoutSingle(userId, checkoutSingleDTO);

        // then
        assertThat(result.getStoreId()).isEqualTo(paymentConfig.getStoreId());
        assertThat(result.getTotalDiscountedAmount()).isEqualTo(totalPrice);
    }

    // 1. 결제 실패 -> 쿠폰 중복 사용
    @Test
    @DisplayName("[실패 케이스] 2개 이상의 상품에 1개의 쿠폰을 중복 사용 - DUPLICATE_USE_USER_COUPON")
    void checkoutSingleUsingDuplicateCoupon_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        appliedCouponForProductIds.add(1L);
        appliedCouponForProductIds.add(1L);
        Long pointAmount = 0L;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;

        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice);

        // when && then
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        assertThat(exception.getErrorType()).isEqualTo(PaymentErrorType.DUPLICATE_USE_USER_COUPON);
    }

    @Test
    @DisplayName("[실패 케이스] 1개 이상의 상품에 1개의 쿠폰을 중복 사용 - DUPLICATE_USE_USER_COUPON")
    void checkoutSingleUsingDuplicateCouponToOneProduct_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        appliedCouponForProductIds.add(1L);
        appliedCouponForProductIds.add(1L);
        Long pointAmount = 0L;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;

        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice);

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        // then
        assertThat(exception.getErrorType()).isEqualTo(PaymentErrorType.DUPLICATE_USE_USER_COUPON);
    }

    // 2. 결제 실패 -> validateCheckoutProduct
    @Test
    @DisplayName("[실패 케이스] 상품이 삭제되어 있는 경우 - DELETED_ORDER_PRODUCT")
    void checkoutSingleDeletedProduct_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        Long pointAmount = 0L;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;

        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice);
        BuyerInfoDTO buyerInfo = getBuyerInfoDTO(userId);
        CheckoutProductInfoDTO productInfo = getCheckoutProductInfoDTO(1L, 1000, 1000, 1L, 1L, ProductStatus.SALES_PROGRESS, true, false, 100L, ProductType.GENERAL);

        when(checkoutCustomRepository.getBuyerInfo(userId)).thenReturn(buyerInfo);
        when(checkoutCustomRepository.getPaymentProductInfo(checkoutSingleDTO.getPaymentProductInfo().getProductId(), checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId(), checkoutSingleDTO.getUsedRegisteredCard(), checkoutSingleDTO.getRegisteredCardId())).thenReturn(productInfo);

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        // then
        assertThat(exception.getErrorType()).isEqualTo(OrderErrorType.DELETED_ORDER_PRODUCT);
    }

    @Test
    @DisplayName("[실패 케이스] 상품의 상태가 판매 중지 상태일 경우 - SALE_STOP_ORDER_PRODUCT")
    void checkoutSingleSalesStop_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        Long pointAmount = 0L;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;

        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice);
        BuyerInfoDTO buyerInfo = getBuyerInfoDTO(userId);
        CheckoutProductInfoDTO productInfoSalesStop = getCheckoutProductInfoDTO(1L, 1000, 1000, 1L, 1L, ProductStatus.SALES_STOP, false, false, 100L, ProductType.GENERAL);


        when(checkoutCustomRepository.getBuyerInfo(userId)).thenReturn(buyerInfo);
        when(checkoutCustomRepository.getPaymentProductInfo(checkoutSingleDTO.getPaymentProductInfo().getProductId(), checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId(), checkoutSingleDTO.getUsedRegisteredCard(), checkoutSingleDTO.getRegisteredCardId())).thenReturn(productInfoSalesStop);

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        // then
        assertThat(exception.getErrorType()).isEqualTo(OrderErrorType.SALE_STOP_ORDER_PRODUCT);
    }

    @Test
    @DisplayName("[실패 케이스] 상품의 상태가 품절인 경우 - SOLD_OUT_ORDER_PRODUCT")
    void checkoutSingleSoldOut_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        Long pointAmount = 0L;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;

        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice);
        BuyerInfoDTO buyerInfo = getBuyerInfoDTO(userId);
        CheckoutProductInfoDTO productInfoSoldOut = getCheckoutProductInfoDTO(1L, 1000, 1000, 1L, 1L, ProductStatus.SOLD_OUT, false, false, 100L, ProductType.GENERAL);


        when(checkoutCustomRepository.getBuyerInfo(userId)).thenReturn(buyerInfo);
        when(checkoutCustomRepository.getPaymentProductInfo(checkoutSingleDTO.getPaymentProductInfo().getProductId(), checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId(), checkoutSingleDTO.getUsedRegisteredCard(), checkoutSingleDTO.getRegisteredCardId())).thenReturn(productInfoSoldOut);

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        // then
        assertThat(exception.getErrorType()).isEqualTo(OrderErrorType.SOLD_OUT_ORDER_PRODUCT);
    }

    @Test
    @DisplayName("[실패 케이스] 상품의 옵션이 삭제되어 있는 경우 - DELETED_ORDER_PRODUCT_OPTION")
    void checkoutSingleDeletedOption_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        Long pointAmount = 0L;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;

        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice);
        BuyerInfoDTO buyerInfo = getBuyerInfoDTO(userId);
        CheckoutProductInfoDTO productInfoDeletedOption = getCheckoutProductInfoDTO(1L, 1000, 1000, 1L, 1L, ProductStatus.SALES_PROGRESS, false, true, 100L, ProductType.GENERAL);


        when(checkoutCustomRepository.getBuyerInfo(userId)).thenReturn(buyerInfo);
        when(checkoutCustomRepository.getPaymentProductInfo(checkoutSingleDTO.getPaymentProductInfo().getProductId(), checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId(), checkoutSingleDTO.getUsedRegisteredCard(), checkoutSingleDTO.getRegisteredCardId())).thenReturn(productInfoDeletedOption);

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        // then
        assertThat(exception.getErrorType()).isEqualTo(OrderErrorType.DELETED_ORDER_PRODUCT_OPTION);
    }

    @Test
    @DisplayName("[실패 케이스] 상품의 재고가 부족한 경우 - OUT_OF_STOCK_ORDER_PRODUCT")
    void checkoutSingleOutOfStock_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        Long pointAmount = 0L;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;

        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 10L, pointAmount, method, (long) discountPrice);
        BuyerInfoDTO buyerInfo = getBuyerInfoDTO(userId);
        CheckoutProductInfoDTO productInfoOutOfStock = getCheckoutProductInfoDTO(1L, 1000, 1000, 1L, 1L, ProductStatus.SALES_PROGRESS, false, false, 5L, ProductType.GENERAL);

        when(checkoutCustomRepository.getBuyerInfo(userId)).thenReturn(buyerInfo);
        when(checkoutCustomRepository.getPaymentProductInfo(checkoutSingleDTO.getPaymentProductInfo().getProductId(), checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId(), checkoutSingleDTO.getUsedRegisteredCard(), checkoutSingleDTO.getRegisteredCardId())).thenReturn(productInfoOutOfStock);

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        // then
        assertThat(exception.getErrorType()).isEqualTo(OrderErrorType.OUT_OF_STOCK_ORDER_PRODUCT);
    }

    // 3. 결제 실패 -> NOT_ENOUGH_POINT_AMOUNT
    @Test
    @DisplayName("[실패 케이스] 사용한 포인트보다 잔액 포인트가 부족할 경우 - NOT_ENOUGH_POINT_AMOUNT")
    void checkoutSingleNotEnoughPointAmount_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        Long pointAmount = 1000L;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;

        // when
        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice);
        BuyerInfoDTO buyerInfo = getBuyerInfoDTO(userId);
        CheckoutProductInfoDTO productInfo = getCheckoutProductInfoDTO(1L, 1000, 1000, 1L, 1L, ProductStatus.SALES_PROGRESS, false, false, 100L, ProductType.GENERAL);
        Long greenLabelPoint = 500L;

        when(checkoutCustomRepository.getBuyerInfo(userId)).thenReturn(buyerInfo);
        when(checkoutCustomRepository.getPaymentProductInfo(checkoutSingleDTO.getPaymentProductInfo().getProductId(), checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId(), checkoutSingleDTO.getUsedRegisteredCard(), checkoutSingleDTO.getRegisteredCardId())).thenReturn(productInfo);
        when(greenLabelPointAllocationService.getGreenLabelPointAmount(userId)).thenReturn(greenLabelPoint);

        // then
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        // then
        assertThat(exception.getErrorType()).isEqualTo(PaymentErrorType.NOT_ENOUGH_POINT_AMOUNT);
    }

    // 4. 결제 실패 -> MISMATCH_TOTAL_AMOUNT
    @Test
    @DisplayName("[실패 케이스] 할인이 적용된 최종 결제 금액이 프론트에서 전해 받은 최종 결제 금액과 일치하지 않는 경우")
    void checkoutSingleMismatchTotalAmount_fail() {
        // given
        Long userId = 1L;
        List<Long> appliedCouponForProductIds = new ArrayList<>();
        appliedCouponForProductIds.add(1L);
        List<Long> appliedCommonUserCouponIds = new ArrayList<>();
        Long pointAmount = 0L;
        int appSalesPrice = 10000;
        int discountPrice = 10000;
        PaymentMethod method = PaymentMethod.CARD;
        CheckoutSingleDTO checkoutSingleDTO = getCheckoutSingleDTO(appliedCouponForProductIds, appliedCommonUserCouponIds, 1L, pointAmount, method, (long) discountPrice - 1000);

        BuyerInfoDTO buyerInfoDTO = getBuyerInfoDTO(userId);
        CheckoutProductInfoDTO checkoutProductInfoDTO = getCheckoutProductInfoDTO(checkoutSingleDTO.getPaymentProductInfo().getProductId(), appSalesPrice, discountPrice, checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId()
                ,ProductStatus.SALES_PROGRESS, false, false, 100L, ProductType.GENERAL);
        List<PaymentCouponDTO> paymentCouponsForProduct = new ArrayList<>();
        List<PaymentCouponDTO> paymentCouponsForOrder = new ArrayList<>();

        Long totalPrice = checkoutProductInfoDTO.getAppSalesPrice() * checkoutSingleDTO.getPaymentProductInfo().getQuantity();
        DiscountInfoDTO discountInfo = DiscountInfoDTO.builder()
                .appSalesPrice((long) appSalesPrice)
                .ecoDiscountAmount(0L)
                .productCouponDiscountAmount(1100L)
                .orderCouponDiscountAmount(0L)
                .pointDiscountAmount(0L)
                .build();

        when(checkoutCustomRepository.getBuyerInfo(userId)).thenReturn(buyerInfoDTO);
        when(checkoutCustomRepository.getPaymentProductInfo(checkoutSingleDTO.getPaymentProductInfo().getProductId(), checkoutSingleDTO.getPaymentProductInfo().getProductOptionId(), checkoutSingleDTO.getPaymentProductInfo().getSellerId(), checkoutSingleDTO.getUsedRegisteredCard(), checkoutSingleDTO.getRegisteredCardId())).thenReturn(checkoutProductInfoDTO);
        when(couponRedeemService.getAmountAfterValidateCouponsForProduct(userId, checkoutSingleDTO.getPaymentProductInfo().getAppliedCouponForProductIds(), checkoutProductInfoDTO.getProductType(), checkoutProductInfoDTO.getMarketName(), checkoutProductInfoDTO.getAppSalesPrice(), checkoutSingleDTO.getPaymentProductInfo().getQuantity())).thenReturn(paymentCouponsForProduct);
        when(couponRedeemService.getAmountAfterValidateCouponsForOrder(userId, checkoutSingleDTO.getAppliedCommonUserCouponIds(), totalPrice)).thenReturn(paymentCouponsForOrder);
        when(greenLabelPointAllocationService.getGreenLabelPointAmount(userId)).thenReturn(0L);
        when(discountService.calculateProductCouponDiscount(checkoutProductInfoDTO.getProductId(), totalPrice, paymentCouponsForProduct)).thenReturn(1100L);
        when(discountService.calculateOrderCouponDiscount(checkoutProductInfoDTO.getProductId(), totalPrice, paymentCouponsForOrder)).thenReturn(0L);
        when(discountService.calculatePointDiscount(checkoutProductInfoDTO.getProductId(), totalPrice, checkoutSingleDTO.getPointAmount())).thenReturn(0L);
        when(discountService.reconcileDiscountAmount(any(ProductPricingDTO.class), any(Long.class), any(Long.class), any(Long.class))).thenReturn(discountInfo);

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                checkoutService.checkoutSingle(userId, checkoutSingleDTO));

        // then
        assertThat(exception.getErrorType()).isEqualTo(PaymentErrorType.MISMATCH_TOTAL_AMOUNT);
    }


    private PaymentOrder getPaymentOrder(Long paymentEventId, Long sellerId, Long productId, Long productOptionHistoryId, Long quantity, Long amount, Long ecoDiscount, Long discountPoint, Long couponDiscount, Integer commissionPercent) {
        return PaymentOrder.builder()
                .id(1L)
                .paymentEventId(paymentEventId)
                .sellerId(sellerId)
                .productId(productId)
                .productOptionHistoryId(productOptionHistoryId)
                .quantity(quantity)
                .orderId(MOCKED_ORDER_ID)
                .amount(amount)
                .ecoDiscount(ecoDiscount)
                .greenLabelDiscount(discountPoint)
                .couponDiscount(couponDiscount)
                .commissionPercent(commissionPercent)
                .status(PaymentOrderStatus.NOT_STARTED)
                .ledgerUpdated(false)
                .walletUpdated(false)
                .isPaymentDone(false)
                .failedCount(0)
                .threshold(5)
                .build();
    }

    private PaymentEvent getPaymentEvent(Long userId, PaymentMethod method) {
        return PaymentEvent.builder()
                .id(1L)
                .buyerId(userId)
                .isPaymentDone(false)
                .paymentKey(MOCKED_PAYMENT_KEY)
                .orderId(MOCKED_ORDER_ID)
                .type(PaymentType.NORMAL)
                .orderName(MOCKED_ORDER_NAME)
                .method(method)
                .build();
    }
    private PaymentCouponDTO getPaymentCouponDTO(Long userCouponId, BenefitType benefitType, Long benefitValue) {
        return new PaymentCouponDTO(userCouponId, benefitType, benefitValue);
    }

    private CheckoutProductInfoDTO getCheckoutProductInfoDTO(Long productId, int appSalesPrice, int discountPrice, Long productOptionId, Long sellerId, ProductStatus productStatus, boolean productIsDeleted, boolean optionIsDeleted, Long stock, ProductType productType) {
        return new CheckoutProductInfoDTO(
                productId,
                sellerId,
                "판매자 브랜드 이름",
                10,
                "상품명 이름",
                productType,
                productStatus,
                appSalesPrice,
                discountPrice,
                0,
                productIsDeleted,
                productOptionId,
                "색상",
                "크기",
                stock,
                optionIsDeleted,
                1L
        );
    }

    private BuyerInfoDTO getBuyerInfoDTO(Long userId) {
        return new BuyerInfoDTO(userId, "email@mm.mm", "구매자 정보", "000-0000-0000");
    }

    private CheckoutSingleDTO getCheckoutSingleDTO(List<Long> appliedCouponForProductIds, List<Long> appliedCommonUserCouponIds, Long quantity, Long pointAmount, PaymentMethod method, Long calculatedTotalAmount) {
        PaymentProductInfoDTO paymentProductInfoDTO = PaymentProductInfoDTO.builder()
                .productId(1L)
                .productOptionId(1L)
                .quantity(quantity)
                .sellerId(1L)
                .appliedCouponForProductIds(appliedCouponForProductIds)
                .build();

        AddressInfoDTO addressInfoDTO = AddressInfoDTO.builder()
                .name("배송지1")
                .receiver("받는 이")
                .postalCode("000-000")
                .address("메인 주소지")
                .detailAddress("상세 주소지")
                .connectNumber("000-0000-0000")
                .memo(null)
                .build();

        return new CheckoutSingleDTO(paymentProductInfoDTO, addressInfoDTO, appliedCommonUserCouponIds, pointAmount, method, false, null, calculatedTotalAmount);
    }
}

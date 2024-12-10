package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.enumType.error.PaymentWebhookErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.entity.product.ProductOption;
import com.impacus.maketplace.entity.product.history.ProductOptionHistory;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.repository.product.ProductOptionRepository;
import com.impacus.maketplace.repository.product.history.ProductOptionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentConfirmService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionHistoryRepository productOptionHistoryRepository;
    private final PaymentOrderHistoryService paymentOrderHistoryService;

    /**
     * 결제 승인 처리
     */
    // transactionId는 어떻게 처리되는 것인가?
    @Transactional
    public void confirm(WebhookPaymentDTO webhookPaymentDTO) {
        // 1. paymentId를 통해서 구매 예정인 상품 조회
        String paymentId = webhookPaymentDTO.getData().getPaymentId();

        PaymentEvent paymentEvent = paymentEventRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new CustomException(PaymentWebhookErrorType.NOT_FOUND_PAYMENT_EVENT_BY_PAYMENT_ID));

        List<PaymentOrder> paymentOrders = paymentOrderRepository.findByPaymentEventId(paymentEvent.getId())
                .orElseThrow(() -> {
                    CustomException exception = new CustomException(PaymentWebhookErrorType.NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID);
                    LogUtils.error(this.getClass() + "confirmService", "paymentEventId와 매핑되는 PaymentOrder를 찾을 수 없습니다.", exception);
                    return exception;
                });

        paymentEvent.getPaymentOrders().addAll(paymentOrders);

        // 2. 결제 상태 확인
        paymentOrders.forEach(paymentOrder -> {
            if (paymentOrder.getStatus() == PaymentOrderStatus.SUCCESS
            || paymentOrder.getStatus() == PaymentOrderStatus.FAILURE) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_FINISH_PAYMENT);
            }
            if (paymentOrder.getStatus() == PaymentOrderStatus.CONFIRM) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_CONFIRM_PROCESS);
            }
        });

        // 3. 결제 금액이 totalAmount와 일치하는지 확인
        if (!validateTotalAmount(paymentEvent, webhookPaymentDTO.getData().getTotalAmount())) {
            paymentEvent.getPaymentOrders()
                    .forEach(paymentOrder -> paymentOrder.changeStatus(PaymentOrderStatus.FAILURE));
            CustomException exception = new CustomException(PaymentErrorType.MISMATCH_TOTAL_AMOUNT);
            LogUtils.error(this.getClass() + ".confirmService mismatch total amount", "paymentID: " + paymentId, exception);
            throw new CustomException(exception);
        }

        // 4. 상품의 재고가 충분한지 확인
        Map<Long, Optional<ProductOption>> productOptionOpts = paymentEvent.getPaymentOrders().stream()
                .collect(Collectors.toMap(
                        PaymentOrder::getProductOptionHistoryId,
                        paymentOrder -> {
                            Long productOptionHistoryId = paymentOrder.getProductOptionHistoryId();
                            Optional<ProductOptionHistory> productOptionHistoryOpt = productOptionHistoryRepository.findById(productOptionHistoryId);
                            return findValidatedProductOption(productOptionHistoryOpt);
                        }
                ));

        Map<Long, ProductOption> productOptions = checkNotFoundedProductOption(paymentEvent, productOptionOpts);

        checkProductStock(paymentEvent, productOptions);

        // 5. 전부 일치한다면 변경해야 되는 상태들 변경해주기(단, 현재 시점에서 구매 확정 X)
        paymentEvent.getPaymentOrders().forEach(paymentOrder -> {
            // 5.1 재고 변경
            ProductOption productOption = productOptions.get(paymentOrder.getProductOptionHistoryId());
            productOption.setStock(productOption.getStock() - paymentOrder.getQuantity());
        });

        // 5.2 Payment Order History Update
        paymentOrderHistoryService.updateAll(paymentEvent.getPaymentOrders(), PaymentOrderStatus.CONFIRM, "payment confirm");

        // 5.3 Payment Order Status 변경
        paymentEvent.getPaymentOrders().forEach(paymentOrder ->
                paymentOrder.changeStatus(PaymentOrderStatus.CONFIRM));

        // 5.4 PaymentEvent 결제 승인 시간 업데이트
        paymentEvent.setApprovedAt(LocalDateTime.now());
    }

    private void checkProductStock(PaymentEvent paymentEvent, Map<Long, ProductOption> productOptions) {
        String message = paymentEvent.getPaymentOrders().stream()
                .filter(paymentOrder -> paymentOrder.getQuantity() > productOptions.get(paymentOrder.getProductOptionHistoryId()).getStock())
                .map(paymentOrder -> "productId: " + paymentOrder.getProductId())
                .collect(joining(", "));

        if (!message.isEmpty()) {
            paymentEvent.getPaymentOrders()
                    .forEach(paymentOrder -> paymentOrder.changeStatus(PaymentOrderStatus.FAILURE));

            CustomException exception = new CustomException(PaymentWebhookErrorType.OUT_OF_STOCK);
            LogUtils.error(this.getClass() + ".confirmService()", "Out of stock\n" + message, exception);
            throw exception;
        }
    }

    private Map<Long, ProductOption> checkNotFoundedProductOption(PaymentEvent paymentEvent, Map<Long, Optional<ProductOption>> productOptions) {
        String message = productOptions.entrySet().stream()
                .filter(longOptionalEntry -> longOptionalEntry.getValue().isEmpty())
                .map(entry -> "productOptionHistoryId: " + entry.getKey())
                .collect(joining(", "));

        if (!message.isEmpty()) {
            paymentEvent.getPaymentOrders().
                    forEach(paymentOrder -> paymentOrder.changeStatus(PaymentOrderStatus.FAILURE));

            CustomException exception = new CustomException(PaymentWebhookErrorType.INVALID_RPODUCT_OPTION);
            LogUtils.error(this.getClass() + ".confirmService()", "Not found productOption\n" + message, exception);
            throw exception;
        }

        return productOptions.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get()));
    }

    private Optional<ProductOption> findValidatedProductOption(Optional<ProductOptionHistory> productOptionHistoryOpt) {
        if (productOptionHistoryOpt.isEmpty()) return Optional.empty();

        ProductOptionHistory productOptionHistory = productOptionHistoryOpt.get();
        ProductOption productOption = productOptionRepository.findProductOptionWithWriteLockById(productOptionHistory.getProductOptionId());

        if (productOption.isDeleted()
                || !productOption.getColor().equals(productOptionHistory.getColor())
                || !productOption.getSize().equals(productOptionHistory.getSize()))
            return Optional.empty();

        return Optional.of(productOption);
    }

    private boolean validateTotalAmount(PaymentEvent paymentEvent, String expectedTotalAmount) {

        Long totalAmount = Long.parseLong(expectedTotalAmount);

        return paymentEvent.getTotalDiscountedAmount().equals(totalAmount);
    }
}

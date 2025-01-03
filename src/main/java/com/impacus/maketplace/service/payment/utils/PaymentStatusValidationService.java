package com.impacus.maketplace.service.payment.utils;

import com.impacus.maketplace.common.enumType.error.PaymentWebhookErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO.*;

@Service
public class PaymentStatusValidationService {

    /**
     * 현재 주문 상태가 올바른지 확인하는 함수
     * 이벤트 타입별로 요구되는 상태가 다르며, 공통적으로 이미 처리된 상태는 예외로 처리한다.
     * @param context  현재 실행 중인 환경
     * @param paymentOrders 주문 리스트
     */
    public void validatePaymentStatus(WebhookEventType context, List<PaymentOrder> paymentOrders) {
        switch (context) {
            case TRANSACTION_READY -> validateReady(paymentOrders);
            case TRANSACTION_CONFIRM -> validateConfirm(paymentOrders);
            case TRANSACTION_PAID -> validatePaid(paymentOrders);
            case TRANSACTION_FAILED -> validateFailed(paymentOrders);
//            case TRANSACTION_PARTIAL_CANCELLED -> validatePartialCancelled(paymentOrders);
//            case TRANSACTION_CANCELLED -> validateCancelled(paymentOrders);
//            case TRANSACTION_PAY_PENDING -> validatePayPending(paymentOrders);
//            case TRANSACTION_CANCEL_PENDING -> validateCancelPending(paymentOrders);
//            case TRANSACTION_VIRTUAL_ACCOUNT_ISSUED -> validateVirtualAccountIssued(paymentOrders);
        }
    }


    private void validateReady(List<PaymentOrder> paymentOrders) {
        for (PaymentOrder order : paymentOrders) {
            // READY 단계에서 허용되지 않는 상태 체크 예:
            // 이미 SUCCESS나 FAILURE 상태인 주문은 준비 상태로 갈 수 없음
            if (order.getStatus() == PaymentOrderStatus.SUCCESS
                    || order.getStatus() == PaymentOrderStatus.FAILURE) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_FINISH_PAYMENT);
            }

            // 또한, CONFIRM 단계로 들어간 주문은 준비 상태로 돌아갈 수 없음
            if (order.getStatus() == PaymentOrderStatus.CONFIRM) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_CONFIRM_PROCESS);
            }

            if (order.getStatus() == PaymentOrderStatus.READY) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_PROCESSED_EVENT);
            }
        }
    }

    private void validateConfirm(List<PaymentOrder> paymentOrders) {
        for (PaymentOrder order : paymentOrders) {
            // CONFIRM 단계에서 허용되지 않는 상태 예:
            // 이미 SUCCESS나 FAILURE라면 재확인 불가
            if (order.getStatus() == PaymentOrderStatus.SUCCESS
                    || order.getStatus() == PaymentOrderStatus.FAILURE) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_FINISH_PAYMENT);
            }

            // 또한, CONFIRM 이전에 READY가 선행되어야 한다.
            if (order.getStatus() != PaymentOrderStatus.READY) {
                throw new CustomException(PaymentWebhookErrorType.INVALID_STATUS_TRANSACTION);
            }
        }
    }

    private void validatePaid(List<PaymentOrder> paymentOrders) {
        for (PaymentOrder order : paymentOrders) {
            // PAID 단계에서 허용되지 않는 상태 예:
            // 이미 SUCCESS나 FAIURE 상태라면 재지정 불가
            if (order.getStatus() == PaymentOrderStatus.SUCCESS
                    || order.getStatus() == PaymentOrderStatus.FAILURE) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_FINISH_PAYMENT);
            }

            // PAID가 되려면 CONFIRM 상태여야 한다.
            if (order.getStatus() != PaymentOrderStatus.CONFIRM) {
                throw new CustomException(PaymentWebhookErrorType.INVALID_STATUS_TRANSACTION);
            }
        }
    }

    private void validateFailed(List<PaymentOrder> paymentOrders) {
        for (PaymentOrder order : paymentOrders) {
            // FAILED 단계에서 허용되지 않는 상태 예:
            // 이미 결제가 완료된 주문은 실패 상태가 될 수 없다.
            if (order.getStatus() == PaymentOrderStatus.SUCCESS) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_FINISH_PAYMENT);
            }

            if (order.getStatus() == PaymentOrderStatus.FAILURE) {
                throw new CustomException(PaymentWebhookErrorType.ALREADY_PROCESSED_EVENT);
            }
        }
    }

    /* 보류 중
    private void validateCancelled(List<PaymentOrder> paymentOrders) {
        for (PaymentOrder order : paymentOrders) {
            // CANCELLED 단계에서 허용되지 않는 상태 예:
            // Webhook 처라 단계에서 이미 SUCCESS 결제가 완료된 상품을 취소하려 하면 문제가 될 수 있다.
            if (order.getStatus() != == PaymentOrderStatus.SUCCESS) {
                throw new CustomException(PaymentWebhookErrorType.CANNOT_CANCEL_SUCCESS_PAYMENT);
            }
        }
    }*/

    private void validateVirtualAccountIssued(List<PaymentOrder> paymentOrders) {

    }

    private void validateCancelPending(List<PaymentOrder> paymentOrders) {

    }

    private void validatePayPending(List<PaymentOrder> paymentOrders) {

    }

    private void validatePartialCancelled(List<PaymentOrder> paymentOrders) {

    }
}

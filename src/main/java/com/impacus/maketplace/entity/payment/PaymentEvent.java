package com.impacus.maketplace.entity.payment;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import com.impacus.maketplace.common.enumType.payment.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
@Table(name = "payment_events")
public class PaymentEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_events_id")
    private Long id;        // PK

    @Column(nullable = false)
    private Long buyerId;   // 구매자 ID

    @Column(nullable = false)
    @ColumnDefault(value = "'false'")
    private Boolean isPaymentDone;  // 결제 완료 여부

    private String paymentKey;  // 멱득성을 보장하기 위한 키

    @Column(unique = true)
    private String orderId;     // 주문 식별자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType type;   // 결제 유형

    private String orderName;   // 주문 이름

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;   // 결제 방식

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> pspRawData;  // PSP로 부터 받은 원시 데이터

    private LocalDateTime approvedAt;   // 결제 승인 시각

    @Transient
    @Builder.Default
    private List<PaymentOrder> paymentOrders = new ArrayList<>();

    /**
     * 주문 상품들의 할인이 적용된 최종 금액 합계(수수료 비용 포함)
     */
    public Long getTotalDiscountedAmountWithCommission() {
        return getTotalDiscountedAmount() - getTotalCommissionFee();
    }


    /**
     * 주문 상품들의 할인이 적용된 최종 금액 합계(수수료 비용 제외)
     */
    public Long getTotalDiscountedAmount() {
        return getTotalAmount() - getTotalDiscount();
    }

    /**
     * 주문 상품들의 할인을 제외한 최종 금액 합계(수수료 비용 제외)
     */
    public Long getTotalAmount() {
        return this.paymentOrders.stream()
                .mapToLong(PaymentOrder::getAmount)
                .sum();
    }

    /**
     * 주문 상품들의 최종 할인 금액 합계
     */
    public Long getTotalDiscount() {
        return getTotalEcoDiscount() + getTotalGreenLabelDiscount() + getTotalCouponDiscount();
    }

    /**
     * 주문 상품들에 적용된 에코 할인 금액 합계
     */
    public Long getTotalEcoDiscount() {
        return this.paymentOrders.stream()
                .mapToLong(PaymentOrder::getEcoDiscount)
                .sum();
    }

    /**
     * 주문 상품들에 적용된 그린 라벨 할인 금액 합계
     */
    public Long getTotalGreenLabelDiscount() {
        return this.paymentOrders.stream()
                .mapToLong(PaymentOrder::getGreenLabelDiscount)
                .sum();
    }

    /**
     * 주문 상품들에 적용된 쿠폰 할인 금액 합계
     */
    public Long getTotalCouponDiscount() {
        return this.paymentOrders.stream()
                .mapToLong(PaymentOrder::getCouponDiscount)
                .sum();
    }

    /**
     * 주문 상품들의 최종 수수료 비용 합계
     */
    public Long getTotalCommissionFee() {
        return this.paymentOrders.stream()
                .mapToLong(paymentOrder -> paymentOrder.getAmount() * paymentOrder.getCommissionPercent() / 100)
                .sum();
    }
}

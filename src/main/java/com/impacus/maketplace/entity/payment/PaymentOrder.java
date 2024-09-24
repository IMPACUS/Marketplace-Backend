package com.impacus.maketplace.entity.payment;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
@Table(name = "payment_orders")
public class PaymentOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_orders_id")
    private Long id;        // PK

    @Column(nullable = false)
    private Long paymentEventId;    // 결제 이벤트 ID

    @Column(nullable = false)
    private Long sellerId;      // 판매자 ID

    @Column(nullable = false)
    private Long productId;     // 상품 ID

    @Column(nullable = false)
    private Long productOptionHistoryId;    // 상품 이력 ID

    @Column(nullable = false)
    @ColumnDefault("'1'")
    private Long quantity;       // 구매 수량

    @Column(nullable = false)
    private String orderId;     // 주문 식별자

    @Column(nullable = false)
    private Long amount;    // 단일 상품 금액

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Long ecoDiscount;   // 에코 할인 금액

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Long greenLabelDiscount;    // 그린 라벨 할인 금액

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Long couponDiscount;    // 쿠폰 할인 금액

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Integer commissionPercent;      // 수수료 퍼센트

    @Column(name = "payment_order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'NOT_STARTED'")
    private PaymentOrderStatus status;  // 결제 주문 상태

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean ledgerUpdated;      // 장부(원장) 업데이트 여부

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean walletUpdated;      // 지갑(정산) 업데이트 여부

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean isPaymentDone;      // 결제 완료 상태

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Integer failedCount;        // 결제 실패 카운트

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Integer threshold;      // 결제 실패 허용 임계값
}

package com.impacus.maketplace.entity.payment;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
public class PaymentOrderHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_order_history_id")
    private Long id;        // PK

    @Column(nullable = false)
    private Long paymentOrderId;    // 결제 주문 ID

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus previousStatus;  // 변경 전 결제 상태

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus newStatus;   // 변경 후 결제 상태

    private String reason;      // 상태 변경 이유
}

package com.impacus.maketplace.entity.order;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.order.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "orders")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;    // 사용자 아이디

    @Column(nullable = false)
    private Integer productCount;   // 주문한 상품 총 수량

    @Column(nullable = false)
    private Long totalOrderAmount;  // 최종 주문 금액

    @Column(nullable = false)
    private Long totalDeliveryFee;  // 최종 배송비

    @Column(nullable = false)
    private Long totalCouponDiscount;   // 최종 쿠폰 할인 금액

    @Column(nullable = false)
    private Long totalGreenLabelPoints; // 최종 적용된 그린 라벨 포인트 금액

    @Column(nullable = false)
    private Long totalEcoDiscount;  // 최종 에코 할인 금액

    @Column(nullable = false)
    private Long totalCommissionFee;    // 최종 적용 수수료 비용

    @Column(nullable = false)
    @ColumnDefault("'PENDING'")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;    // 결재 상태
}

package com.impacus.maketplace.entity.order;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id; // 주문 아이디

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 사용자

    @Column(name = "total_production_cost")
    private Integer totalProductCost; // 상품 총 가격

    @Column(name = "delivery_fee")
    private Integer deliveryFee; // 배송비

    @Column(name = "received_point")
    private Integer receivedPoint;

    @Column(name = "discount_amount")
    private Integer discountAmount; // 할인 금액 => 쿠폰 할인 ex) 정액 할인, 정률 할인

    @Column(name = "used_point")
    private Integer usedPoint; // 사용한 포인트


}

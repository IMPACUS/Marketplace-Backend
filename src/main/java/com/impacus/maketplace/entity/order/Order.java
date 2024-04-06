package com.impacus.maketplace.entity.order;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.OrderStatus;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.common.utils.TimestampConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id; // 주문 아이디

    @Column(nullable = false)
    private Long userId; // 사용자 아이디

    @Column(nullable = false)
    private Long shoppingBasketId; // 장바구니 아이디

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문 상태

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // 결제 방식

    @Convert(converter = TimestampConverter.class)
    private LocalDate orderCancelDate; // 주문 취소 날짜

    @Convert(converter = TimestampConverter.class)
    private LocalDate orderCompleteDate; // 주문 완료 날짜

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    private String orderCancelReason; // 주문 취소 사유

}

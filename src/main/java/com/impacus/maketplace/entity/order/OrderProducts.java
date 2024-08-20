package com.impacus.maketplace.entity.order;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.DiscountType;
import com.impacus.maketplace.common.enumType.order.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
public class OrderProducts extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_products_id")
    private Long id;

    @Column(nullable = false)
    private Long ordersId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long productOptionHistoryId;

    @Column(nullable = false)
    private Long productPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Long greenLabelDiscount;

    @Column(nullable = false)
    private Long individualCouponDiscount;

    @Column(nullable = false)
    private Long totalCouponDiscount;

    @Column(nullable = false)
    private Boolean isEcoProduct;

    @Column(nullable = false)
    @ColumnDefault("'AMOUNT'")
    @Enumerated(EnumType.STRING)
    private DiscountType ecoDiscountType;

    @Column(nullable = false)
    private Long ecoDiscountAmount;

    @Column(nullable = false)
    private Long shippingFee;

    @Column(nullable = false)
    private Integer commissionPercent;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private Integer returnQuantity;

    @Column(nullable = false)
    private Integer exchangeQuantity;
}

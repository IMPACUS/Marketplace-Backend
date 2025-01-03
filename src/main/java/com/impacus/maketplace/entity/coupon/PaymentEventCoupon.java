package com.impacus.maketplace.entity.coupon;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "payment_event_coupons")
public class PaymentEventCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_event_coupons_id")
    private Long id;

    @Column(nullable = false)
    private Long paymentEventId;

    @Column(nullable = false)
    private Long userCouponId;

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean isUsed;

    public void markAsUsed() {
        this.isUsed = true;
    }
}

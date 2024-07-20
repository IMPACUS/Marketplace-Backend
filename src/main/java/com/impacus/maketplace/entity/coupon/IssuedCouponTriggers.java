package com.impacus.maketplace.entity.coupon;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class IssuedCouponTriggers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long issuedCouponHistoryId;

    @Column(nullable = false)
    private Long triggerId;
}

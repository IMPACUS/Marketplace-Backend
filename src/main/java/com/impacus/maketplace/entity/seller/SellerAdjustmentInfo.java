package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.enumType.BankCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "seller_adjustment_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerAdjustmentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_business_info_id")
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BankCode bankCode;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String accountNumber;
}


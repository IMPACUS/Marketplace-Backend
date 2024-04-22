package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.enumType.seller.BusinessType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "seller_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String contactName; // 판매 담당자 이름

    @Column(nullable = false)
    private String marketName;

    @Column(nullable = false)
    private Long logoImageId;

    private String customerServiceName; // 고객센터 전화 번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;
}

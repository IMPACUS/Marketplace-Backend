package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.enumType.seller.SellerType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String contactName;

    @Column(nullable = false)
    private String marketName;

    @Column(nullable = false)
    private Long brandLogoImageId; // 변경 가능: Base64로 저장 시, String으로 될 수도 있음.

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SellerType sellerType;
}

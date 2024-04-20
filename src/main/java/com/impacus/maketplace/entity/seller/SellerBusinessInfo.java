package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.enumType.BankCode;

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
@Table(name = "seller_business_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerBusinessInfo {
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

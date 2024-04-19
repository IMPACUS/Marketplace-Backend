package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.enumType.seller.BusinessType;

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
@Table(name = "seller_adjustment_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerAdjustmentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_adjustment_info")
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private String representativeName;

    @Column(nullable = false)
    private String representativeContact;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String businessRegistrationNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @Column(nullable = false)
    private String businessAddress;

    @Column(nullable = false)
    private String businessEmail;

    @Column(nullable = false)
    private String copyBusinessRegistrationCertificate; //Base64 인코딩 파일

    @Column(nullable = false)
    private String copyMainOrderBusinessReportCard;

    @Column(nullable = false)
    private String mailOrderBusinessReportNumber;
}

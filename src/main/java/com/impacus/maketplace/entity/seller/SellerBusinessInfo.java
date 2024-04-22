package com.impacus.maketplace.entity.seller;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "seller_business_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerBusinessInfo {
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
    private String businessCondition;

    @Column(nullable = false)
    private String businessAddress;

    @Column(nullable = false)
    private String businessEmail;

    @Column(nullable = false)
    private Long copyBusinessRegistrationCertificateId; // 사업자 등록증 사본 이미지 id

    @Column(nullable = true)
    private Long copyMainOrderBusinessReportCardId; // 통신 판매업 신고증 id

    @Column(nullable = false)
    private String mailOrderBusinessReportNumber;
}

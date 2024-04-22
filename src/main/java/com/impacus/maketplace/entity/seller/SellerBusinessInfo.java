package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.converter.AES256ToStringConverter;
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
    @Convert(converter = AES256ToStringConverter.class)
    private String representativeContact;

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    private String businessName; // 상호명

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    private String businessRegistrationNumber; // 사업자 등록증 번호

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    private String businessCondition; // 업태

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    private String businessAddress; // 사업자 등록 주소

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    private String businessEmail; // 이메일 (fax 확인용)

    @Column(nullable = false)
    private Long copyBusinessRegistrationCertificateId; // 사업자 등록증 사본 이미지 id

    private Long copyMainOrderBusinessReportCardId; // 통신 판매업 신고증 id

    @Convert(converter = AES256ToStringConverter.class)
    private String mailOrderBusinessReportNumber; // 통신 판매업 번호
}

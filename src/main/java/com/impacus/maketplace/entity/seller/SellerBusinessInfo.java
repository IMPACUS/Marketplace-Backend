package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Table(name = "seller_business_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerBusinessInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_adjustment_info")
    private Long id;

    @Column(nullable = false, unique = true)
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
    @Comment("사업자 등록증 번호")
    private String businessRegistrationNumber;

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    @Comment("업태")
    private String businessCondition;

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    @Comment("사업자 등록 주소")
    private String businessAddress;

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    @Comment("이메일 (fax 확인용)")
    private String businessEmail;

    @Column(nullable = false)
    @Comment("사업자 등록증 사본 이미지 id")
    private Long copyBusinessRegistrationCertificateId;

    @Comment("통신 판매업 신고증 id")
    private Long copyMainOrderBusinessReportCardId;

    @Convert(converter = AES256ToStringConverter.class)
    @Comment("통신 판매업 번호")
    private String mailOrderBusinessReportNumber;
}

package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.converter.AES256ToStringConverter;
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

    @Convert(converter = AES256ToStringConverter.class)
    @Column(nullable = false)
    private String accountName;

    @Convert(converter = AES256ToStringConverter.class)
    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Long copyBankBookId; // 통장 사본 이미지 id


}


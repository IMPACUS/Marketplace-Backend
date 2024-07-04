package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.BankCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Table(name = "seller_adjustment_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerAdjustmentInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_business_info_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long sellerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BankCode bankCode;

    @Convert(converter = AES256ToStringConverter.class)
    @Column(nullable = false)
    @Comment("계좌 명")
    private String accountName;

    @Convert(converter = AES256ToStringConverter.class)
    @Column(nullable = false)
    @Comment("계좌 번호")
    private String accountNumber;

    @Column(nullable = false)
    @Comment("통장 사본 이미지 id")
    private Long copyBankBookId;
}


package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.BankCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    private String accountName;

    @Convert(converter = AES256ToStringConverter.class)
    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Long copyBankBookId; // 통장 사본 이미지 id

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부
}


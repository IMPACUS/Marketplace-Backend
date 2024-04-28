package com.impacus.maketplace.entity.seller;

import org.hibernate.annotations.ColumnDefault;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.BankCode;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
public class SellerAdjustmentInfo extends BaseEntity {
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

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부
}


package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "seller_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntryStatus entryStatus; // 입점 상태

    @Column(nullable = false)
    private String contactName; // 판매 담당자 이름

    @Column(nullable = false)
    private String marketName;

    @Column(nullable = false)
    private Long logoImageId;

    private String customerServiceNumber; // 고객센터 전화 번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BusinessType businessType; // 사업자 구분

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    @Builder
    public Seller(Long userId,
                  String contactName,
                  String marketName,
                  Long logoImageId,
                  String customerServiceNumber,
                  BusinessType businessType) {
        this.userId = userId;
        this.contactName = contactName;
        this.marketName = marketName;
        this.logoImageId = logoImageId;
        this.customerServiceNumber = customerServiceNumber;
        this.businessType = businessType;
        this.entryStatus = EntryStatus.REQUEST;
        this.isDeleted = false;

    }
}

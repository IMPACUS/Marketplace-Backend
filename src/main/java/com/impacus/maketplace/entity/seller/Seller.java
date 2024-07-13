package com.impacus.maketplace.entity.seller;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "seller")
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
    @Comment("입점 상태")
    private EntryStatus entryStatus;

    @Column(nullable = false)
    @Comment("판매 담당자 이름")
    private String contactName;

    @Column(nullable = false)
    private String marketName;

    @Column(nullable = false)
    private Long logoImageId;

    @Comment("고객센터 전화 번호")
    private String customerServiceNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("사업자 구분")
    private BusinessType businessType;

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    @Comment("삭제 여부")
    private boolean isDeleted; //

    @ColumnDefault("0")
    @Column(nullable = false, name = "charge_percent")
    @Comment("수수료 비율")
    private int chargePercent;

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
        this.chargePercent = 0;
    }
}

package com.impacus.maketplace.entity.product.bundleDelivery;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.product.DeliveryFeeRule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "bundle_delivery_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BundleDeliveryGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bundle_delivery_group_id")
    private Long id;

    @Comment("그룹명")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String groupNumber;

    @Column(nullable = false)
    private Long sellerId;

    @Comment("사용여부")
    @Column(nullable = false, name = "is_used")
    private boolean isUsed;

    @Comment("배송비계산방식")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryFeeRule deliveryFeeRule;

    @Comment("삭제여부")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;
}

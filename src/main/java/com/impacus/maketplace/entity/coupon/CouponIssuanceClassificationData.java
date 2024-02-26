package com.impacus.maketplace.entity.coupon;


import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.coupon.CouponIssuanceClassification;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.lang.annotation.control.CodeGenerationHint;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CouponIssuanceClassificationData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cic_id")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "cic_code")
    private CouponIssuanceClassification type;
}

package com.impacus.maketplace.entity.seller.deliveryCompany;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "seller_delivery_company")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerDeliveryCompany extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "seller_delivery_company_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private Long sellerId;

  @Column(nullable = false)
  @Comment("일반 배송비")
  private int generalDeliveryFee;

  @Column(nullable = false)
  @Comment("일반 특수 배송비")
  private int generalSpecialDeliveryFee;

  @Column(nullable = false)
  @Comment("반품 배송비")
  private int refundDeliveryFee;

  @Column(nullable = false)
  @Comment("반품 특수 배송비")
  private int refundSpecialDeliveryFee;

  @Builder
  public SellerDeliveryCompany(
          Long sellerId,
          int generalDeliveryFee,
          int generalSpecialDeliveryFee,
          int refundDeliveryFee,
          int refundSpecialDeliveryFee
  ) {
    this.sellerId = sellerId;
    this.generalDeliveryFee = generalDeliveryFee;
    this.generalSpecialDeliveryFee = generalSpecialDeliveryFee;
    this.refundDeliveryFee = refundDeliveryFee;
    this.refundSpecialDeliveryFee = refundSpecialDeliveryFee;
  }
}

package com.impacus.maketplace.entity.seller.deliveryCompany;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
}

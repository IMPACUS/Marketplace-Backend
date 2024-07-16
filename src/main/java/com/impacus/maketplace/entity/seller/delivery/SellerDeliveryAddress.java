package com.impacus.maketplace.entity.seller.delivery;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.BankCode;
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
@Table(name = "seller_delivery_address")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerDeliveryAddress extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "seller_delivery_address_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private Long sellerId;

  @Column(nullable = false)
  @Comment("일반 주소지")
  private String generalAddress;

  @Column(nullable = false)
  @Comment("일반 상세 주소")
  private String generalDetailAddress;

  @Column(nullable = false)
  @Comment("일반 상호명")
  private String generalBusinessName;

  @Column(nullable = false)
  @Comment("반품 주소지")
  private String refundAddress;

  @Column(nullable = false)
  @Comment("반품 상세 주소지")
  private String refundDetailAddress;

  @Column(nullable = false)
  @Comment("반품 상호명")
  private String refundBusinessName;

  @Column(nullable = false)
  @Comment("반품 배송비 계쫘")
  private String refundAccountNumber;

  @Column(nullable = false)
  @Comment("반품 배송비 예금주명")
  private String refundAccountName;

  @Column(nullable = false)
  @Comment("반품 계좌 은행")
  private BankCode refundBankCode;
}

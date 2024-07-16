package com.impacus.maketplace.entity.seller.delivery;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.BankCode;
import jakarta.persistence.*;
import lombok.*;
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

  @Column(nullable = false)
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
  @Comment("반품 배송비 계좌")
  private String refundAccountNumber;

  @Column(nullable = false)
  @Comment("반품 배송비 예금주명")
  private String refundAccountName;

  @Column(nullable = false)
  @Comment("반품 계좌 은행")
  private BankCode refundBankCode;

  @Builder
  public SellerDeliveryAddress(
          Long sellerId,
          String generalAddress,
          String generalDetailAddress,
          String generalBusinessName,
          String refundAddress,
          String refundDetailAddress,
          String refundBusinessName,
          String refundAccountNumber,
          String refundAccountName,
          BankCode refundBankCode
  ) {
    this.sellerId = sellerId;
    this.generalAddress = generalAddress;
    this.generalDetailAddress = generalDetailAddress;
    this.generalBusinessName = generalBusinessName;
    this.refundAddress = refundAddress;
    this.refundDetailAddress = refundDetailAddress;
    this.refundBusinessName = refundBusinessName;
    this.refundAccountNumber = refundAccountNumber;
    this.refundAccountName = refundAccountName;
    this.refundBankCode = refundBankCode;
  }
}

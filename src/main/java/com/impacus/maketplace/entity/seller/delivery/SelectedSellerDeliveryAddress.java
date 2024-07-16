package com.impacus.maketplace.entity.seller.delivery;

import com.impacus.maketplace.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "selected_seller_delivery_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectedSellerDeliveryAddress extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "selected_seller_delivery_address_id")
  private Long id;

  @Column(nullable = false)
  private Long sellerId;

  @Column(nullable = false)
  private Long sellerDeliveryAddressId;

  public SelectedSellerDeliveryAddress(Long sellerId, Long sellerDeliveryAddressId) {
    this.sellerId = sellerId;
    this.sellerDeliveryAddressId = sellerDeliveryAddressId;
  }

  public static SelectedSellerDeliveryAddress toEntity(Long sellerId, Long sellerDeliveryAddressId) {
    return new SelectedSellerDeliveryAddress(sellerId, sellerDeliveryAddressId);
  }
}

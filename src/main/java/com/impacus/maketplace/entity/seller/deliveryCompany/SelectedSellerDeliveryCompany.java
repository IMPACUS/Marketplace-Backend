package com.impacus.maketplace.entity.seller.deliveryCompany;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "selected_seller_delivery_company")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectedSellerDeliveryCompany extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "selected_seller_delivery_company_id")
  private Long id;

  @Column(nullable = false)
  private Long sellerDeliveryCompanyId;

  @Column(nullable = false)
  @Comment("택배사")
  @Enumerated(EnumType.STRING)
  private DeliveryCompany deliveryCompany;

  @Column(nullable = false)
  @Comment("순서")
  private int displayOrder;

  public SelectedSellerDeliveryCompany(
          Long sellerDeliveryCompanyId,
          DeliveryCompany deliveryCompany,
          int displayOrder
  ) {
    this.sellerDeliveryCompanyId = sellerDeliveryCompanyId;
    this.deliveryCompany = deliveryCompany;
    this.displayOrder = displayOrder;
  }
}

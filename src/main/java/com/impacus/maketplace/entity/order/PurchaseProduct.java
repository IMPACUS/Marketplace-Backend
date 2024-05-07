package com.impacus.maketplace.entity.order;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.entity.product.ProductDetailInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "purchase_product")
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_product_id")
    private Long id; // 주문 상품 아이디

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order; // 주문

    @JoinColumn(name = "product_detail_info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDetailInfo productDetailInfo; // 상품 상세 정보

    @Column(name = "quantity")
    private Integer quantity; // 수량

    @Column(name = "delivery_status")
    private String deliveryStatus; // 배송 상태

    @Column(name = "return_status")
    private String returnStatus; // 반품 상태

    @Column(name = "exchange_status")
    private String exchangeStatus; // 교환 상태

    @Column(name = "total_price")
    private Integer totalPrice; // 총 가격

}

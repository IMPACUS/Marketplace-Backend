package com.impacus.maketplace.repository.payment.checkout.dto;

import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class PaymentProductInfoDTO {
    private Long productId; // 상품 id
    private Long sellerId;  // 판매자 id
    private Integer chargePercent;  // 수수료 비율
    private String name; // 상품명
    private ProductStatus productStatus; // 상품 상태
    private int appSalesPrice; // 앱 판매가
    private int discountPrice; // 할인가
    private Integer deliveryFee; // 배송비
    private boolean productIsDeleted; // 삭제 여부
    private Long productOptionId;   // 상품 option id
    private String color;   // 색상
    private String size;    // 크기
    private Long stock; // 재고 수량
    private boolean optionIsDeleted;    // 옵션 삭제 여부
    private Long productOptionHistoryId;    // 상품 옵션 history id

    @QueryProjection
    public PaymentProductInfoDTO(Long productId, Long sellerId, Integer chargePercent, String name, ProductStatus productStatus, int appSalesPrice, int discountPrice, Integer deliveryFee, boolean productIsDeleted, Long productOptionId, String color, String size, Long stock, boolean optionIsDeleted, Long productOptionHistoryId) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.chargePercent = chargePercent;
        this.name = name;
        this.productStatus = productStatus;
        this.appSalesPrice = appSalesPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.productIsDeleted = productIsDeleted;
        this.productOptionId = productOptionId;
        this.color = color;
        this.size = size;
        this.stock = stock;
        this.optionIsDeleted = optionIsDeleted;
        this.productOptionHistoryId = productOptionHistoryId;
    }
}

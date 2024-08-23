package com.impacus.maketplace.dto.order.response;

import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.order.querydsl.dto.OrderProductWithDetailsDTO;
import lombok.Data;

@Data
public class CheckoutProductDTO {
    private Long productId;   // 상품 id
    private String name;    // 상품명
    private String marketName;  // 브랜드명
    private String color;   // 색상
    private String size;    // 크기
    private Long productOptionId;   // 상품 옵션 id
    private Long quantity; // 구매 수량
    private ProductType type; // 상품 타입
    private DiscountStatus discountStatus;  // 할인 상태
    private Integer deliveryFee; // 배송비
    private int appSalesPrice; // 앱 판매가
    private int discountPrice; // 할인가
    private String productImage; // 대표 이미지

    public CheckoutProductDTO(OrderProductWithDetailsDTO orderProductWithDetailsDTO, Long productId, Long productOptionId, Long quantity) {
        this.productId = productId;
        this.name = orderProductWithDetailsDTO.getName();
        this.marketName = orderProductWithDetailsDTO.getMarketName();
        this.color = orderProductWithDetailsDTO.getColor();
        this.size = orderProductWithDetailsDTO.getSize();
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.type = orderProductWithDetailsDTO.getType();
        this.discountStatus = orderProductWithDetailsDTO.getDiscountStatus();
        this.deliveryFee = orderProductWithDetailsDTO.getDeliveryFee();
        this.appSalesPrice = orderProductWithDetailsDTO.getAppSalesPrice();
        this.discountPrice = orderProductWithDetailsDTO.getDiscountPrice();
        if (!orderProductWithDetailsDTO.getProductImages().isEmpty()) {
            this.productImage = orderProductWithDetailsDTO.getProductImages().get(0);
        }
    }

    public CheckoutProductDTO(OrderProductWithDetailsByCartDTO orderProductWithDetailsByCartDTO) {
        this.productId = orderProductWithDetailsByCartDTO.getProductId();
        this.name = orderProductWithDetailsByCartDTO.getName();
        this.marketName = orderProductWithDetailsByCartDTO.getMarketName();
        this.color = orderProductWithDetailsByCartDTO.getColor();
        this.size = orderProductWithDetailsByCartDTO.getSize();
        this.productOptionId = orderProductWithDetailsByCartDTO.getProductOptionId();
        this.quantity = orderProductWithDetailsByCartDTO.getQuantity();
        this.type = orderProductWithDetailsByCartDTO.getType();
        this.discountStatus = orderProductWithDetailsByCartDTO.getDiscountStatus();
        this.deliveryFee = orderProductWithDetailsByCartDTO.getDeliveryFee();
        this.appSalesPrice = orderProductWithDetailsByCartDTO.getAppSalesPrice();
        this.discountPrice = orderProductWithDetailsByCartDTO.getDiscountPrice();
        if (!orderProductWithDetailsByCartDTO.getProductImages().isEmpty()) {
            this.productImage = orderProductWithDetailsByCartDTO.getProductImages().get(0);
        }
    }
}

package com.impacus.maketplace.dto.payment.response;

import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsByCartDTO;
import com.impacus.maketplace.repository.payment.checkout.dto.CheckoutProductWithDetailsDTO;
import lombok.Data;

@Data
public class CheckoutProductDTO {
    private Long productId;   // 상품 id
    private String name;    // 상품명
    private Long sellerId;  // 판매자 id
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

    public CheckoutProductDTO(CheckoutProductWithDetailsDTO checkoutProductWithDetailsDTO, Long productId, Long productOptionId, Long quantity) {
        this.productId = productId;
        this.name = checkoutProductWithDetailsDTO.getName();
        this.sellerId = checkoutProductWithDetailsDTO.getSellerId();
        this.marketName = checkoutProductWithDetailsDTO.getMarketName();
        this.color = checkoutProductWithDetailsDTO.getColor();
        this.size = checkoutProductWithDetailsDTO.getSize();
        this.productOptionId = productOptionId;
        this.quantity = quantity;
        this.type = checkoutProductWithDetailsDTO.getType();
        this.discountStatus = checkoutProductWithDetailsDTO.getDiscountStatus();
        this.deliveryFee = checkoutProductWithDetailsDTO.getDeliveryFee();
        this.appSalesPrice = checkoutProductWithDetailsDTO.getAppSalesPrice();
        this.discountPrice = checkoutProductWithDetailsDTO.getDiscountPrice();
        if (!checkoutProductWithDetailsDTO.getProductImages().isEmpty()) {
            this.productImage = checkoutProductWithDetailsDTO.getProductImages().get(0);
        }
    }

    public CheckoutProductDTO(CheckoutProductWithDetailsByCartDTO orderProductWithDetailsByCartDTO) {
        this.productId = orderProductWithDetailsByCartDTO.getProductId();
        this.name = orderProductWithDetailsByCartDTO.getName();
        this.sellerId = orderProductWithDetailsByCartDTO.getSellerId();
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

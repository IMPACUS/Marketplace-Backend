package com.impacus.maketplace.repository.order.querydsl.dto;

import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class OrderProductWithDetailsByCartDTO {
    private Long productId; // 상품 id
    private String name; // 상품명
    private ProductStatus productStatus; // 상품 상태
    private ProductType type; // 상품 타입
    private DiscountStatus discountStatus; // 할인 상태
    private int appSalesPrice; // 앱 판매가
    private int discountPrice; // 할인가
    private Integer deliveryFee; // 배송비
    private List<String> productImages; // 이미지 리스트
    private boolean productIsDeleted; // 삭제 여부
    private String marketName; // 브랜드명, 마켓명
    private String color;   // 색상
    private String size;    // 크기
    private Long stock; // 재고 수량
    private boolean optionIsDeleted;    // 옵션 삭제 여부
    // OrderProductWithDetailsDTO에서 추가된 필드
    private Long productOptionId;   // 상품 옵션 id
    private Long quantity; // 구매 수량

    @QueryProjection
    public OrderProductWithDetailsByCartDTO(Long productId, String name, ProductStatus productStatus, ProductType type, DiscountStatus discountStatus, int appSalesPrice, int discountPrice, Integer deliveryFee, List<String> productImages, boolean productIsDeleted, String marketName, String color, String size, Long stock, boolean optionIsDeleted, Long productOptionId, Long quantity) {
        this.productId = productId;
        this.name = name;
        this.productStatus = productStatus;
        this.type = type;
        this.discountStatus = discountStatus;
        this.appSalesPrice = appSalesPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.productImages = productImages;
        this.productIsDeleted = productIsDeleted;
        this.marketName = marketName;
        this.color = color;
        this.size = size;
        this.stock = stock;
        this.optionIsDeleted = optionIsDeleted;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
    }
}

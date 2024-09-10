package com.impacus.maketplace.dto.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewSellerDTO {
    private Long id; // 리뷰 인덱스 번호
//    private Long orderId; // 주문 인덱스 번호
    private Long sellerId; // 판매자 인덱스 번호
    private Long buyerId; // 구매자 인덱스 번호
    private Integer score; // 점수
    private String buyerContents; // 구매자 리뷰 내용
    private Long buyerUploadImgId; // 구매자 업로드 이미지 번호
    private String sellerComment; // 판매자 답글
    private ZonedDateTime createAt; // 리뷰 생성일
    private Boolean isArchive; // 삭제 시 아카이브 여부
    private ZonedDateTime archiveAt; // 아카이브 시작점 (Spring 스케쥴링 기법으로 자동 삭제 여부 확인)

    private String buyerName; // 주문자 표시 (웹 - 판매자 사이트)
    private String idName; // 주문자 아이디

    // 아래는 product_detail_info
    private String productColor;
    private Long productId;
    private String productMaterial;
    private String productSize;
    private String productType;

    // 아래는 purchase_product
    private Integer quantity;
    private Integer totalPrice;
}

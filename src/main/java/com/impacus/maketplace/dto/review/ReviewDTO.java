package com.impacus.maketplace.dto.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * 여기는 form 양식으로 활용하기 위한 용도!!
 * 어차피 필요 없는 값은 아예 null 값으로 한다
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {
    private Long id; // 리뷰 인덱스 번호
    private Long orderId; // 주문 인덱스 번호
    private Long sellerId; // 판매자 인덱스 번호
    private Integer score; // 점수
    private String buyerContents; // 구매자 리뷰 내용
    private Long buyerUploadImgId; // 구매자 업로드 이미지 번호
    private String sellerComment; // 판매자 답글
    private ZonedDateTime createAt; // 리뷰 생성일
    private Boolean isArchive; // 삭제 시 아카이브 여부
    private ZonedDateTime archiveAt; // 아카이브 시작점 (Spring 스케쥴링 기법으로 자동 삭제 여부 확인)

    private String buyerName; // 주문자 표시 (웹 - 판매자 사이트)
    private String idName; // 주문자 아이디

}

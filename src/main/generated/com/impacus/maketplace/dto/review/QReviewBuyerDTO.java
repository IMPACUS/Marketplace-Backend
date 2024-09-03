package com.impacus.maketplace.dto.review;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.review.QReviewBuyerDTO is a Querydsl Projection type for ReviewBuyerDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReviewBuyerDTO extends ConstructorExpression<ReviewBuyerDTO> {

    private static final long serialVersionUID = 96338445L;

    public QReviewBuyerDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<Long> orderId, com.querydsl.core.types.Expression<Integer> score, com.querydsl.core.types.Expression<String> buyerContents, com.querydsl.core.types.Expression<Long> buyerUploadImgId, com.querydsl.core.types.Expression<String> sellerComment) {
        super(ReviewBuyerDTO.class, new Class<?>[]{long.class, long.class, int.class, String.class, long.class, String.class}, id, orderId, score, buyerContents, buyerUploadImgId, sellerComment);
    }

}


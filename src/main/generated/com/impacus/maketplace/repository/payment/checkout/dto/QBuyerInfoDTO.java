package com.impacus.maketplace.repository.payment.checkout.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.repository.payment.checkout.dto.QBuyerInfoDTO is a Querydsl Projection type for BuyerInfoDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBuyerInfoDTO extends ConstructorExpression<BuyerInfoDTO> {

    private static final long serialVersionUID = 1722189063L;

    public QBuyerInfoDTO(com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> phoneNumber) {
        super(BuyerInfoDTO.class, new Class<?>[]{long.class, String.class, String.class, String.class}, userId, email, name, phoneNumber);
    }

}


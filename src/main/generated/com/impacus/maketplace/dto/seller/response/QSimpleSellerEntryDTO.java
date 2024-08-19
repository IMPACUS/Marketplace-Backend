package com.impacus.maketplace.dto.seller.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.seller.response.QSimpleSellerEntryDTO is a Querydsl Projection type for SimpleSellerEntryDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSimpleSellerEntryDTO extends ConstructorExpression<SimpleSellerEntryDTO> {

    private static final long serialVersionUID = -789009879L;

    public QSimpleSellerEntryDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<java.time.LocalDateTime> requestAt, com.querydsl.core.types.Expression<String> marketName, com.querydsl.core.types.Expression<String> contactNumber, com.querydsl.core.types.Expression<String> businessCondition, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.seller.EntryStatus> entryStatus) {
        super(SimpleSellerEntryDTO.class, new Class<?>[]{long.class, java.time.LocalDateTime.class, String.class, String.class, String.class, com.impacus.maketplace.common.enumType.seller.EntryStatus.class}, id, requestAt, marketName, contactNumber, businessCondition, entryStatus);
    }

}


package com.impacus.maketplace.dto.admin;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.admin.QAdminGroupCountDTO is a Querydsl Projection type for AdminGroupCountDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAdminGroupCountDTO extends ConstructorExpression<AdminGroupCountDTO> {

    private static final long serialVersionUID = -410247892L;

    public QAdminGroupCountDTO(com.querydsl.core.types.Expression<String> accountType, com.querydsl.core.types.Expression<Long> count) {
        super(AdminGroupCountDTO.class, new Class<?>[]{String.class, long.class}, accountType, count);
    }

}


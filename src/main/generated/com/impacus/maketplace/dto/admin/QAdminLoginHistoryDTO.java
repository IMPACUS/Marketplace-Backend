package com.impacus.maketplace.dto.admin;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.admin.QAdminLoginHistoryDTO is a Querydsl Projection type for AdminLoginHistoryDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAdminLoginHistoryDTO extends ConstructorExpression<AdminLoginHistoryDTO> {

    private static final long serialVersionUID = 167523569L;

    public QAdminLoginHistoryDTO(com.querydsl.core.types.Expression<Long> adminId, com.querydsl.core.types.Expression<String> status) {
        super(AdminLoginHistoryDTO.class, new Class<?>[]{long.class, String.class}, adminId, status);
    }

    public QAdminLoginHistoryDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<java.time.ZonedDateTime> crtDate, com.querydsl.core.types.Expression<String> status) {
        super(AdminLoginHistoryDTO.class, new Class<?>[]{long.class, java.time.ZonedDateTime.class, String.class}, id, crtDate, status);
    }

}


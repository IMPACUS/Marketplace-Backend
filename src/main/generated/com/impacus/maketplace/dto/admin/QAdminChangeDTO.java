package com.impacus.maketplace.dto.admin;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.admin.QAdminChangeDTO is a Querydsl Projection type for AdminChangeDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAdminChangeDTO extends ConstructorExpression<AdminChangeDTO> {

    private static final long serialVersionUID = -143325748L;

    public QAdminChangeDTO(com.querydsl.core.types.Expression<Long> adminId, com.querydsl.core.types.Expression<String> accountType, com.querydsl.core.types.Expression<java.time.ZonedDateTime> recentActivityDate) {
        super(AdminChangeDTO.class, new Class<?>[]{long.class, String.class, java.time.ZonedDateTime.class}, adminId, accountType, recentActivityDate);
    }

}


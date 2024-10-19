package com.impacus.maketplace.dto.admin;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.admin.QAdminLoginActivityDTO is a Querydsl Projection type for AdminLoginActivityDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAdminLoginActivityDTO extends ConstructorExpression<AdminLoginActivityDTO> {

    private static final long serialVersionUID = -370554678L;

    public QAdminLoginActivityDTO(com.querydsl.core.types.Expression<Long> adminId, com.querydsl.core.types.Expression<java.time.ZonedDateTime> crtDate, com.querydsl.core.types.Expression<String> activityDetail) {
        super(AdminLoginActivityDTO.class, new Class<?>[]{long.class, java.time.ZonedDateTime.class, String.class}, adminId, crtDate, activityDetail);
    }

    public QAdminLoginActivityDTO(com.querydsl.core.types.Expression<java.time.ZonedDateTime> crtDate, com.querydsl.core.types.Expression<String> activityDetail) {
        super(AdminLoginActivityDTO.class, new Class<?>[]{java.time.ZonedDateTime.class, String.class}, crtDate, activityDetail);
    }

}


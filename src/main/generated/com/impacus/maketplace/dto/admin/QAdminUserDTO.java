package com.impacus.maketplace.dto.admin;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.admin.QAdminUserDTO is a Querydsl Projection type for AdminUserDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAdminUserDTO extends ConstructorExpression<AdminUserDTO> {

    private static final long serialVersionUID = 664583953L;

    public QAdminUserDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> accountType, com.querydsl.core.types.Expression<String> phoneNumber, com.querydsl.core.types.Expression<java.time.ZonedDateTime> recentActivityDate, com.querydsl.core.types.Expression<String> activityDetail) {
        super(AdminUserDTO.class, new Class<?>[]{long.class, String.class, String.class, String.class, String.class, java.time.ZonedDateTime.class, String.class}, id, name, email, accountType, phoneNumber, recentActivityDate, activityDetail);
    }

}


package com.impacus.maketplace.dto.user.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.user.response.QWebUserDTO is a Querydsl Projection type for WebUserDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWebUserDTO extends ConstructorExpression<WebUserDTO> {

    private static final long serialVersionUID = 46112407L;

    public QWebUserDTO(com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> phoneNumber, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.user.UserLevel> userLevel, com.querydsl.core.types.Expression<java.time.LocalDateTime> registerAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> recentLoginAt) {
        super(WebUserDTO.class, new Class<?>[]{long.class, String.class, String.class, String.class, com.impacus.maketplace.common.enumType.user.UserLevel.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, userId, name, email, phoneNumber, userLevel, registerAt, recentLoginAt);
    }

}


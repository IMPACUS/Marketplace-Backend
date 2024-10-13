package com.impacus.maketplace.dto.common.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.common.response.QAttachFileDTO is a Querydsl Projection type for AttachFileDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAttachFileDTO extends ConstructorExpression<AttachFileDTO> {

    private static final long serialVersionUID = -1962346489L;

    public QAttachFileDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> fileURL) {
        super(AttachFileDTO.class, new Class<?>[]{long.class, String.class}, id, fileURL);
    }

}


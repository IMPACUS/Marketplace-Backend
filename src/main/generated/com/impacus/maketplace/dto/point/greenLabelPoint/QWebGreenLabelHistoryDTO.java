package com.impacus.maketplace.dto.point.greenLabelPoint;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.point.greenLabelPoint.QWebGreenLabelHistoryDTO is a Querydsl Projection type for WebGreenLabelHistoryDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWebGreenLabelHistoryDTO extends ConstructorExpression<WebGreenLabelHistoryDTO> {

    private static final long serialVersionUID = -450274666L;

    public QWebGreenLabelHistoryDTO(com.querydsl.core.types.Expression<Long> historyId, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.point.PointType> pointType, com.querydsl.core.types.Expression<Long> tradeAmount, com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(WebGreenLabelHistoryDTO.class, new Class<?>[]{long.class, com.impacus.maketplace.common.enumType.point.PointType.class, long.class, long.class, String.class, String.class, java.time.LocalDateTime.class}, historyId, pointType, tradeAmount, userId, email, name, createdAt);
    }

}


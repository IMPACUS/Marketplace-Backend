package com.impacus.maketplace.dto.point.greenLabelPoint;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.impacus.maketplace.dto.point.greenLabelPoint.QWebGreenLabelHistoryDetailDTO is a Querydsl Projection type for WebGreenLabelHistoryDetailDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWebGreenLabelHistoryDetailDTO extends ConstructorExpression<WebGreenLabelHistoryDetailDTO> {

    private static final long serialVersionUID = -924911803L;

    public QWebGreenLabelHistoryDetailDTO(com.querydsl.core.types.Expression<Long> historyId, com.querydsl.core.types.Expression<Long> greenLabelPoint, com.querydsl.core.types.Expression<Long> levelPoint, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.point.PointType> pointType, com.querydsl.core.types.Expression<Long> tradeAmount, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<com.impacus.maketplace.common.enumType.point.PointStatus> pointStatus, com.querydsl.core.types.Expression<String> orderId) {
        super(WebGreenLabelHistoryDetailDTO.class, new Class<?>[]{long.class, long.class, long.class, com.impacus.maketplace.common.enumType.point.PointType.class, long.class, java.time.LocalDateTime.class, com.impacus.maketplace.common.enumType.point.PointStatus.class, String.class}, historyId, greenLabelPoint, levelPoint, pointType, tradeAmount, createAt, pointStatus, orderId);
    }

}


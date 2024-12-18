package com.impacus.maketplace.dto.point.greenLabelPoint;

import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import com.impacus.maketplace.common.enumType.point.GrantMethod;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WebGreenLabelHistoryDTO {
    private Long historyId;         // 포인트 지급 아이디

    @ExcelColumn(headerName = "공급자")
    private String provider;        // 공급자

    @ExcelColumn(headerName = "조건")
    private String issueCondition;  // 지급 조건

    @ExcelColumn(headerName = "포인트")
    private long tradeAmount;           // 포인트

    private Long userId;

    @ExcelColumn(headerName = "아이디")
    private String email;           // 아이디

    @ExcelColumn(headerName = "성함")
    private String name;            // 성함

    @ExcelColumn(headerName = "지급 상태")
    private RewardPointStatus status;   // 발급 상태

    @ExcelColumn(headerName = "지급 일자")
    private LocalDateTime createdAt; // 지급 일자

    @QueryProjection
    public WebGreenLabelHistoryDTO(
            Long historyId,
            PointType pointType,
            long tradeAmount,
            Long userId,
            String email,
            String name,
            LocalDateTime createdAt
    ) {
        this.historyId = historyId;
        this.provider = "아임플레이스";
        this.issueCondition = pointType.getValue();
        this.tradeAmount = tradeAmount;
        this.userId = userId;
        this.email = email.split("_")[1];
        this.name = name;
        this.status = RewardPointStatus.COMPLETED;
        if (pointType.getRewardPointType() != null && pointType.getRewardPointType().getGrantMethod() == GrantMethod.MANUAL) {
            this.status = RewardPointStatus.MANUAL;
        }

        this.createdAt = createdAt;
    }
}

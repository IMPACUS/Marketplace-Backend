package com.impacus.maketplace.dto.point.greenLabelPoint;

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
    private String provider;        // 공급자
    private String issueCondition;  // 지급 조건
    private long point;           // 포인트
    private Long userId;
    private String email;           // 아이디
    private String name;            // 성함
    private RewardPointStatus status;   // 발급 상태
    private LocalDateTime createdAt; // 지급 일자

    @QueryProjection
    public WebGreenLabelHistoryDTO(
            Long historyId,
            PointType pointType,
            long point,
            Long userId,
            String email,
            String name,
            LocalDateTime createdAt
    ) {
        this.historyId = historyId;
        this.provider = "아임플레이스";
        this.issueCondition = pointType.getValue();
        this.point = point;
        this.userId = userId;
        this.email = email.split("_")[1];
        this.name = name;
        this.status = RewardPointStatus.COMPLETED;
        this.createdAt = createdAt;
    }
}

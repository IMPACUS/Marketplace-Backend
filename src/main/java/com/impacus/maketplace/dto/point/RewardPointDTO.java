package com.impacus.maketplace.dto.point;

import com.impacus.maketplace.common.enumType.point.GrantMethod;
import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RewardPointDTO {
    private Long rewardPointId;         // 포인트 리워드 아이디
    private Long point;                 // 포인트
    private String name; // 포인트 명
    private String issueCondition;      // 지급 조건
    private long expirationPeriod;    // 사용 기간
    private long issueQuantity;         // 지급 수
    private GrantMethod grantMethod;         // 자동/수동 (AUTO, MANUAL)
    private RewardPointStatus status;              // 발급 상태
    private LocalDateTime createAt;     // 등록일

    @QueryProjection
    public RewardPointDTO(
            Long rewardPointId,
            RewardPointType rewardPointType,
            Duration expirationPeriod,
            long issueQuantity,
            GrantMethod grantMethod,
            RewardPointStatus status,
            LocalDateTime createAt
    ) {
        this.rewardPointId = rewardPointId;
        this.point = rewardPointType.getAllocatedPoints();
        this.name = rewardPointType.getValue();
        this.issueCondition = this.point == null ? rewardPointType.getIssueCondition() : String.format("%s[%dP]", rewardPointType.getIssueCondition(), this.point);
        this.expirationPeriod = expirationPeriod.toDays();
        this.issueQuantity = issueQuantity;
        this.grantMethod = grantMethod;
        this.status = status;
        this.createAt = createAt;
    }
}

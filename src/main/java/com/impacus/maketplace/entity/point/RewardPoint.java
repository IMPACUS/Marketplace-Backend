package com.impacus.maketplace.entity.point;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.point.GrantMethod;
import com.impacus.maketplace.common.enumType.point.RewardPointStatus;
import com.impacus.maketplace.common.enumType.point.RewardPointType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.Duration;

@Entity
@Getter
@Table(name = "reward_point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardPoint extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_point_id")
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RewardPointType rewardPointType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RewardPointStatus status;

    @Column(nullable = false)
    @Comment("지급 수")
    private long issueQuantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("발급 상태")
    private GrantMethod grantMethod;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    @Column(nullable = false)
    private Duration expirationPeriod;

    public RewardPoint(RewardPointType rewardPointType) {
        this.rewardPointType = rewardPointType;
        this.status = RewardPointStatus.ISSUING;
        this.issueQuantity = 0;
        this.grantMethod = rewardPointType.getGrantMethod();
        this.isDeleted = false;
        this.expirationPeriod = rewardPointType.getExpirationPeriod();
    }

    public static RewardPoint from(RewardPointType rewardPointType) {
        return new RewardPoint(rewardPointType);
    }

    public void incrementIssueQuantity() {
        this.issueQuantity++;
    }
}

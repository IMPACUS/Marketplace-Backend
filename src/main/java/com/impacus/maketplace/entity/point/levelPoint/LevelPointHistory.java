package com.impacus.maketplace.entity.point.levelPoint;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "level_point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelPointHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_point_history_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    @Column(nullable = false)
    private Long tradeAmount;

    @Column(nullable = false)
    @ColumnDefault("FALSE")
    @Comment("등급 상승 및 달성 포인트 받았는지 여부")
    private boolean hasReceivedLevelUpPoints;

    public LevelPointHistory(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradeAmount,
            boolean hasReceivedLevelUpPoints
    ) {
        this.userId = userId;
        this.pointType = pointType;
        this.pointStatus = pointStatus;
        this.tradeAmount = tradeAmount;
        this.hasReceivedLevelUpPoints = hasReceivedLevelUpPoints;
    }

    public static LevelPointHistory toEntity(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradeAmount,
            boolean hasReceivedLevelUpPoints
    ) {
        return new LevelPointHistory(userId, pointType, pointStatus, tradeAmount, hasReceivedLevelUpPoints);
    }
}

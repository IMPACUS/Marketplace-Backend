package com.impacus.maketplace.entity.point.greenLablePoint;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;


@Entity
@Getter
@Table(name = "green_label_point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GreenLabelPointHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "green_label_point_history_id")
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
    @Comment("미적용 포인트")
    private Long unappliedPoint;

    @Column(nullable = false)
    @Comment("레벨 포인트 (해당 시점 기준)")
    private long levelPoint;

    @Column(nullable = false)
    @Comment("그린 라벨 포인트 (해당 시점 기준)")
    private long greenLabelPoint;

    public GreenLabelPointHistory(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradeAmount,
            Long unappliedPoint,
            long greenLabelPoint,
            long levelPoint
    ) {
        this.userId = userId;
        this.pointType = pointType;
        this.pointStatus = pointStatus;
        this.tradeAmount = tradeAmount;
        this.unappliedPoint = unappliedPoint;
        this.greenLabelPoint = greenLabelPoint;
        this.levelPoint = levelPoint;
    }

    public static GreenLabelPointHistory of(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradeAmount,
            Long unappliedPoint,
            long greenLabelPoint,
            long levelPoint
    ) {
        return new GreenLabelPointHistory(
                userId, pointType, pointStatus, tradeAmount, unappliedPoint, greenLabelPoint, levelPoint
        );
    }
}

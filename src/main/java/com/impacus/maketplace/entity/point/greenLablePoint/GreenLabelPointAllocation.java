package com.impacus.maketplace.entity.point.greenLablePoint;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "green_label_point_allocation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GreenLabelPointAllocation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "green_label_point_allocation_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointUsageStatus pointStatus;

    @Column(nullable = false)
    @Comment("남은 포인트")
    private Long remainPoint;

    @Column(nullable = false)
    @Comment("지급 포인트")
    private Long allocatedPoint;

    @Comment("포인트 소멸 시작일")
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Comment("최근 포인트 소멸일")
    @Column(nullable = true)
    private LocalDateTime lastExpiredAt;

    public GreenLabelPointAllocation(
            Long userId,
            PointType pointType,
            Long allocatedPoint
    ) {
        this.userId = userId;
        this.pointType = pointType;
        this.pointStatus = PointUsageStatus.UNUSED;
        this.allocatedPoint = allocatedPoint;
        this.remainPoint = allocatedPoint;
        this.expiredAt = LocalDateTime.now().plusMonths(6);
    }

    public static GreenLabelPointAllocation of(
            Long userId,
            PointType pointType,
            Long allocatedPoint
    ) {
        return new GreenLabelPointAllocation(userId, pointType, allocatedPoint);
    }
}

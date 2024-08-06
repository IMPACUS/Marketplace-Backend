package com.impacus.maketplace.entity.point.greenLablePoint;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "green_label_point_history_relation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GreenLabelPointHistoryRelation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "green_label_point_history_relation_id")
    private Long id;

    @Column(nullable = false)
    private Long greenLabelPointAllocationId;

    @Column(nullable = false)
    private Long greenLabelPointHistoryId;

    @Column(nullable = false)
    private LocalDateTime previousExpiredAt;

    public GreenLabelPointHistoryRelation(
            Long greenLabelPointAllocationId,
            Long greenLabelPointHistoryId,
            LocalDateTime previousExpiredAt
    ) {
        this.greenLabelPointAllocationId = greenLabelPointAllocationId;
        this.greenLabelPointHistoryId = greenLabelPointHistoryId;
        this.previousExpiredAt = previousExpiredAt;
    }

    public static GreenLabelPointHistoryRelation of(
            Long greenLabelPointAllocationId,
            Long greenLabelPointHistoryId,
            LocalDateTime previousExpiredAt
    ) {
        return new GreenLabelPointHistoryRelation(
                greenLabelPointAllocationId, greenLabelPointHistoryId, previousExpiredAt
        );
    }
}

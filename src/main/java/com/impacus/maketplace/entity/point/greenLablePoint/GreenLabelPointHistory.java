package com.impacus.maketplace.entity.point.greenLablePoint;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    private PointStatus pointStatus;

    @Column(nullable = false)
    private int tradeAmount;
}

package com.impacus.maketplace.entity.point;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.PointType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Long id;

    @Column(nullable = false)
    private Long pointMasterId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PointType pointType; // POINT_TYPE [10 : 적립, 20 : 사용, 30 : 소멸]

    @Column(nullable = false)
    private Integer changePoint; // 변동 포인트

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isManual;
}

package com.impacus.maketplace.entity.point;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "point_master")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointMaster extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_master_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "user_score")
    @ColumnDefault("0")
    private Integer userScore; // 유저 스코어 (유저의 레벨과 진행도 및 퍼센트를 나타내기 위함)

    @ColumnDefault("false")
    private boolean isBronze;

    @ColumnDefault("false")
    private boolean isRookie;

    @ColumnDefault("false")
    private boolean isSilver;

    @ColumnDefault("false")
    private boolean isGold;

    @ColumnDefault("false")
    private boolean isEcoVip;

    @Column(name = "avl_point")
    @ColumnDefault("0")
    private Integer availablePoint; // 가용 포인트

    private transient String registerId;

    private transient String modifyId;


}

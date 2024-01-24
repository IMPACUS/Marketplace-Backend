package com.impacus.maketplace.entity.point;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@ToString(callSuper = true)
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
    @Setter
    private Integer userScore; // 유저 스코어 (유저의 레벨과 진행도 및 퍼센트를 나타내기 위함)

    @ColumnDefault("false")
    @Setter
    private boolean isBronze;

    @ColumnDefault("false")
    @Setter
    private boolean isRookie;

    @ColumnDefault("false")
    @Setter
    private boolean isSilver;

    @ColumnDefault("false")
    @Setter
    private boolean isGold;

    @ColumnDefault("false")
    @Setter
    private boolean isEcoVip;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Setter
    private UserLevel userLevel = UserLevel.BRONZE;

    @Column(name = "avl_point")
    @ColumnDefault("0")
    @Setter
    private Integer availablePoint; // 가용 포인트

    private transient String registerId;

    private transient String modifyId;




}

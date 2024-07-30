package com.impacus.maketplace.entity.point.levelPoint;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "level_achievement")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelAchievement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_achievement_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    private boolean achievedBronze;
    private boolean achievedRookie;
    private boolean achievedSilver;
    private boolean achievedGold;
    private boolean achievedVip;

    private LocalDateTime recentAchievedBronzeAt;
    private LocalDateTime recentAchievedRookieAt;
    private LocalDateTime recentAchievedSilverAt;
    private LocalDateTime recentAchievedGoldAt;
    private LocalDateTime recentAchievedVipAt;

    public LevelAchievement(Long userId) {
        this.userId = userId;
    }

    public static LevelAchievement toEntity(Long userId) {
        return new LevelAchievement(userId);
    }
}

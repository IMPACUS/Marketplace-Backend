package com.impacus.maketplace.entity.point.levelPoint;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "level_point_master")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelPointMaster extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_point_master_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    @Column(nullable = false)
    private Long levelPoint;

    @Comment("포인트 소멸 시작일")
    @Column(nullable = false)
    private LocalDateTime expirationStartAt;

    public LevelPointMaster(Long userId) {
        this.userId = userId;
        this.userLevel = UserLevel.BRONZE;
        this.levelPoint = 0L;
        this.expirationStartAt = LocalDateTime.now();
    }

    public static LevelPointMaster toEntity(Long userId) {
        return new LevelPointMaster(userId);
    }
}

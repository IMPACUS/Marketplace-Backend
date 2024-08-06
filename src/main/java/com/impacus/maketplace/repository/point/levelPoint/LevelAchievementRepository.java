package com.impacus.maketplace.repository.point.levelPoint;

import com.impacus.maketplace.entity.point.levelPoint.LevelAchievement;
import com.impacus.maketplace.repository.point.levelPoint.querydsl.LevelAchievementCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelAchievementRepository extends JpaRepository<LevelAchievement, Long>, LevelAchievementCustomRepository {

    Optional<LevelAchievement> findByUserId(Long userId);
}

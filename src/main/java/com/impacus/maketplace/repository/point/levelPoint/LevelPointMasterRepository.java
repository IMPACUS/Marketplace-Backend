package com.impacus.maketplace.repository.point.levelPoint;

import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointMaster;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LevelPointMasterRepository extends JpaRepository<LevelPointMaster, Long>{

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT lpm FROM LevelPointMaster lpm where lpm.userId = :userId")
    Optional<LevelPointMaster> findByUserIdForUpdate(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE LevelPointMaster lpm " +
            "SET lpm.levelPoint = :levelPoint, lpm.userLevel =:userLevel, lpm.expirationStartAt = :expirationStartAt " +
            "WHERE lpm.userId = :userId")
    int updateLevelPointAndExpiredAt(
            @Param("levelPoint") Long levelPoint,
            @Param("userLevel") UserLevel userLevel,
            @Param("expirationStartAt") LocalDateTime expirationStartAt,
            @Param("userId") Long userId
    );
}

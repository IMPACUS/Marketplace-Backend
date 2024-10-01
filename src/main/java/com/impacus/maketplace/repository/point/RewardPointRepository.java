package com.impacus.maketplace.repository.point;

import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.impacus.maketplace.entity.point.RewardPoint;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardPointRepository extends JpaRepository<RewardPoint, Long> {

    @Query("select rp.rewardPointType FROM RewardPoint rp")
    List<RewardPointType> findRewardPointType();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RewardPoint r WHERE r.rewardPointType = :type")
    RewardPoint findByRewardPointType(@Param("type") RewardPointType type);
}

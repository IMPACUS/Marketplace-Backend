package com.impacus.maketplace.repository.point;

import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.impacus.maketplace.entity.point.RewardPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardPointRepository extends JpaRepository<RewardPoint, Long> {

    @Query("select rp.rewardPointType FROM RewardPoint rp")
    List<RewardPointType> findRewardPointType();
}

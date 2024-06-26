package com.impacus.maketplace.repository.temporaryProduct;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductClaimInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryProductClaimRepository extends JpaRepository<TemporaryProductClaimInfo, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE TemporaryProductClaimInfo tpc " +
            "SET tpc.recallInfo = :recallInfo, tpc.claimCost = :claimCost, tpc.claimPolicyGuild = :claimPolicyGuild, tpc.claimContactInfo = :claimContactInfo " +
            "WHERE tpc.temporaryProductId = :id")
    int updateTemporaryProductClaim(
            @Param("id") Long id,
            @Param("recallInfo") String recallInfo,
            @Param("claimCost") String claimCost,
            @Param("claimPolicyGuild") String claimPolicyGuild,
            @Param("claimContactInfo") String claimContactInfo
    );

}

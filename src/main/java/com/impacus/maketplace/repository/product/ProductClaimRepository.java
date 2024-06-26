package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.ProductClaimInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductClaimRepository extends JpaRepository<ProductClaimInfo, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE ProductClaimInfo tpc " +
            "SET tpc.recallInfo = :recallInfo, tpc.claimCost = :claimCost, tpc.claimPolicyGuild = :claimPolicyGuild, tpc.claimContactInfo = :claimContactInfo " +
            "WHERE tpc.productId = :id")
    int updateProductClaimInfo(
            @Param("id") Long id,
            @Param("recallInfo") String recallInfo,
            @Param("claimCost") String claimCost,
            @Param("claimPolicyGuild") String claimPolicyGuild,
            @Param("claimContactInfo") String claimContactInfo
    );
}

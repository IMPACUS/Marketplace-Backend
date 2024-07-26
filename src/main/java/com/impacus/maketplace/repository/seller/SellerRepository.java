package com.impacus.maketplace.repository.seller;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.seller.mapping.SellerMarketNameViewsMapping;
import com.impacus.maketplace.repository.seller.querydsl.ReadSellerCustomRepository;
import com.impacus.maketplace.repository.seller.querydsl.UpdateSellerCustomRepository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>, ReadSellerCustomRepository, UpdateSellerCustomRepository {
    Long countByCreateAtBetweenAndIsDeletedIsFalse(LocalDateTime start, LocalDateTime end);

    Long countByEntryStatusAndIsDeletedIsFalse(EntryStatus entryStatus);

    Optional<Seller> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Seller s SET s.entryStatus = :entryStatus, s.chargePercent = :chargePercent WHERE s.id = :id")
    int updateSellerEntryStatusAndChargePercent(
            @Param("id") Long id,
            @Param("entryStatus") EntryStatus entryStatus,
            @Param("chargePercent") int chargePercent
    );

    @Query("SELECT s.id, s.marketName FROM Seller s WHERE s.idDelete = false ORDER BY s.marketName")
    List<SellerMarketNameViewsMapping> findMarketNames();
}

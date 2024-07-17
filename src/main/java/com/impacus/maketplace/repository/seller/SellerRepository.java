package com.impacus.maketplace.repository.seller;


import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.seller.querydsl.SellerReadCustomRepository;
import com.impacus.maketplace.repository.seller.querydsl.SellerWriteCustomRepository;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>, SellerReadCustomRepository, SellerWriteCustomRepository {
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
}

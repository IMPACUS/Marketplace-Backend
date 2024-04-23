package com.impacus.maketplace.repository.seller;


import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.repository.seller.querydsl.SellerCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>, SellerCustomRepository {
    Long countByCreateAtBetweenAndIsDeletedIsFalse(LocalDateTime start, LocalDateTime end);

    Long countByEntryStatusAndIsDeletedIsFalse(EntryStatus entryStatus);

    Optional<Seller> findByUserId(Long userId);

}

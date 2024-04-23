package com.impacus.maketplace.repository.seller;


import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.entity.seller.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Long countByCreateAtBetweenAndIsDeletedIsFalse(LocalDateTime start, LocalDateTime end);

    Long countByEntryStatusAndIsDeletedIsFalse(EntryStatus entryStatus); //DeletedFalse

}

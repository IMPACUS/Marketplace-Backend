package com.impacus.maketplace.repository.seller;

import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.entity.seller.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    int countByCreateAtBetweenAndIsDeletedFalse(LocalDateTime start, LocalDateTime end);
}

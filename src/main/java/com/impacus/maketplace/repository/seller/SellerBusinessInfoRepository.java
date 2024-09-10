package com.impacus.maketplace.repository.seller;

import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerBusinessInfoRepository extends JpaRepository<SellerBusinessInfo, Long> {

    Optional<SellerBusinessInfo> findBySellerId(Long sellerId);
}

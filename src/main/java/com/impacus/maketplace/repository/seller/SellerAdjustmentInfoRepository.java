package com.impacus.maketplace.repository.seller;

import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerAdjustmentInfoRepository extends JpaRepository<SellerAdjustmentInfo, Long> {
}

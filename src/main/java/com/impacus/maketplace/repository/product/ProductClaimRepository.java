package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.ProductClaimInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductClaimRepository extends JpaRepository<ProductClaimInfo, Long> {
}

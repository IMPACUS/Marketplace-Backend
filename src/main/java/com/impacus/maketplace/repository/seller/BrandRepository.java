package com.impacus.maketplace.repository.seller;

import com.impacus.maketplace.entity.seller.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    /**
     * sellerId로 Brand가 존재하는지 확인하는 함수
     *
     * @param sellerId
     * @return
     */
    boolean existsBySellerId(Long sellerId);

    Optional<Brand> findBySellerId(Long sellerId);
}

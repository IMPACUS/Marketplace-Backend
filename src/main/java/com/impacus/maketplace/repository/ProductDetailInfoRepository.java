package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.product.ProductDetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailInfoRepository extends JpaRepository<ProductDetailInfo, Long> {
    List<ProductDetailInfo> findByProductId(Long productId);
}

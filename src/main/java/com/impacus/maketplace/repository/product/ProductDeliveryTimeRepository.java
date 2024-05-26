package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.ProductDeliveryTime;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDeliveryTimeRepository extends JpaRepository<ProductDeliveryTime, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE ProductDeliveryTime pd SET pd.minDays = :minDays, pd.maxDays = :maxDays WHERE pd.productId = :id")
    int updateProductDeliveryTime(
            @Param("id") Long id,
            @Param("minDays") int minDays,
            @Param("maxDays") int maxDays
    );

    List<ProductDeliveryTime> findByProductId(Long productId);
}

package com.impacus.maketplace.repository.temporaryProduct;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDeliveryTime;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryProductDeliveryTimeRepository extends JpaRepository<TemporaryProductDeliveryTime, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE TemporaryProductDeliveryTime tpd SET tpd.minDays = :minDays, tpd.maxDays = :maxDays WHERE tpd.temporaryProductId = :id")
    int updateTemporaryProductDeliveryTime(
            @Param("id") Long id,
            @Param("minDays") int minDays,
            @Param("maxDays") int maxDays
    );

    List<TemporaryProductDeliveryTime> findByTemporaryProductId(Long temporaryProductId);
}

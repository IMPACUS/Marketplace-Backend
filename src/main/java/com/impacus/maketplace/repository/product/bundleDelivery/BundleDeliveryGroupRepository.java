package com.impacus.maketplace.repository.product.bundleDelivery;

import com.impacus.maketplace.entity.product.bundleDelivery.BundleDeliveryGroup;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BundleDeliveryGroupRepository extends JpaRepository<BundleDeliveryGroup, Long> {
    Optional<BundleDeliveryGroup> findByIsDeletedFalseAndId(Long id);

    boolean existsByNameAndSellerIdAndIsDeletedFalse(String name, Long sellerId);

    @Transactional
    @Modifying
    @Query("UPDATE BundleDeliveryGroup bdg SET bdg.isDeleted = true WHERE bdg.id = :id")
    int updateIsDeleteTrueById(
            @Param("id") Long id
    );
}

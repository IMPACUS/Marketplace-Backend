package com.impacus.maketplace.repository.product.bundleDelivery;

import com.impacus.maketplace.entity.product.bundleDelivery.BundleDeliveryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BundleDeliveryGroupRepository extends JpaRepository<BundleDeliveryGroup, Long> {
    Optional<BundleDeliveryGroup> findByIsDeletedFalseAndId(Long id);
}

package com.impacus.maketplace.repository.product.bundleDelivery;

import com.impacus.maketplace.entity.product.bundleDelivery.BundleDeliveryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BundleDeliveryGroupRepository extends JpaRepository<BundleDeliveryGroup, Long> {
}

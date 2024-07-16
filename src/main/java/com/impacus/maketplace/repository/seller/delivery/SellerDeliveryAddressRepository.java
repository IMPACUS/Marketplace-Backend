package com.impacus.maketplace.repository.seller.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.entity.seller.delivery.SellerDeliveryAddress;

@Repository
public interface SellerDeliveryAddressRepository extends JpaRepository<SellerDeliveryAddress, Long> {

    boolean existsBySellerIdAndId(Long sellerId, Long id);
}

package com.impacus.maketplace.repository.seller.delivery;

import com.impacus.maketplace.entity.seller.delivery.SellerDeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerDeliveryAddressRepository extends JpaRepository<SellerDeliveryAddress, Long> {
}

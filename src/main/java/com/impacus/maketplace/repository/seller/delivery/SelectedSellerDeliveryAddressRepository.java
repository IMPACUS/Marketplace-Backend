package com.impacus.maketplace.repository.seller.delivery;

import com.impacus.maketplace.entity.seller.delivery.SelectedSellerDeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedSellerDeliveryAddressRepository extends JpaRepository<SelectedSellerDeliveryAddress, Long> {
}

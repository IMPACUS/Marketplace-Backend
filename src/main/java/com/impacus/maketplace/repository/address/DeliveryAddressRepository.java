package com.impacus.maketplace.repository.address;

import com.impacus.maketplace.entity.address.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
}

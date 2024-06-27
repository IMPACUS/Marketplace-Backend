package com.impacus.maketplace.repository.order;

import com.impacus.maketplace.entity.order.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
}

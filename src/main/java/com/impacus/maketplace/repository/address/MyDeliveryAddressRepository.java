package com.impacus.maketplace.repository.address;

import com.impacus.maketplace.entity.address.MyDeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyDeliveryAddressRepository extends JpaRepository<MyDeliveryAddress, Long> {

    List<MyDeliveryAddress> findAllByUserId(Long userId);

    Optional<MyDeliveryAddress> findByUserIdAndName(Long userId, String name);

    Long countByUserId(Long userId);

    Optional<MyDeliveryAddress> findByIdAndUserId(Long id, Long userId);

    Boolean existsByUserIdAndName(Long userId, String name);
}

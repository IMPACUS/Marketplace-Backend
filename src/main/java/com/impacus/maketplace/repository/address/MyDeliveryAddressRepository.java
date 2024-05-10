package com.impacus.maketplace.repository.address;

import com.impacus.maketplace.entity.address.MyDeliveryAddress;
import com.impacus.maketplace.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyDeliveryAddressRepository extends JpaRepository<MyDeliveryAddress, Long> {

    Optional<MyDeliveryAddress> findByIdAndUser(Long id, User user);

    List<MyDeliveryAddress> findAllByUser(User proxyUser);

}

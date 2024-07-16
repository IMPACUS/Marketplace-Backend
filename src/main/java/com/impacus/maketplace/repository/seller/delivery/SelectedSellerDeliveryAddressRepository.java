package com.impacus.maketplace.repository.seller.delivery;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.entity.seller.delivery.SelectedSellerDeliveryAddress;

import jakarta.transaction.Transactional;

@Repository
public interface SelectedSellerDeliveryAddressRepository extends JpaRepository<SelectedSellerDeliveryAddress, Long> {
    Optional<SelectedSellerDeliveryAddress> findBySellerId(Long sellerId);

    @Transactional
    @Modifying
    @Query("UPDATE SelectedSellerDeliveryAddress ssda SET ssda.sellerDeliveryAddressId = :sellerDeliveryAddressId WHERE ssda.id = :id")
    int updateSellerDeliveryAddressIdById(@Param("id") Long id, @Param("sellerDeliveryAddressId") Long sellerDeliveryAddressId);
}

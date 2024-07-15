package com.impacus.maketplace.repository.seller.deliveryCompany;

import com.impacus.maketplace.entity.seller.deliveryCompany.SellerDeliveryCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerDeliveryCompanyRepository extends JpaRepository<SellerDeliveryCompany, Long> {
    Optional<SellerDeliveryCompany> findBySellerId(Long sellerId);
}

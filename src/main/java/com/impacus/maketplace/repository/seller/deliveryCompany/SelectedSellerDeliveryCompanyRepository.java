package com.impacus.maketplace.repository.seller.deliveryCompany;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.entity.seller.deliveryCompany.SelectedSellerDeliveryCompany;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectedSellerDeliveryCompanyRepository extends JpaRepository<SelectedSellerDeliveryCompany, Long> {
    List<SelectedSellerDeliveryCompany> findBySellerDeliveryCompanyIdOrderByDisplayOrder(Long sellerDeliveryCompanyId);

    @Transactional
    @Modifying
    @Query("UPDATE SelectedSellerDeliveryCompany ssdc SET ssdc.deliveryCompany = :deliveryCompany WHERE ssdc.id = :id")
    int updateDeliveryCompanyById(@Param("id") Long id, @Param("deliveryCompany") DeliveryCompany deliveryCompany);
}

package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.ProductDeliveryTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDeliveryTimeRepository extends JpaRepository<ProductDeliveryTime, Long> {
}

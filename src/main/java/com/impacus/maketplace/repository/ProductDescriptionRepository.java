package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.product.ProductDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {

    List<ProductDescription> findByProductId(Long productId);
}

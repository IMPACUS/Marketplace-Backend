package com.impacus.maketplace.repository;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryProductRepository extends JpaRepository<TemporaryProduct, Long> {

    boolean existsByRegisterId(String userId);
}

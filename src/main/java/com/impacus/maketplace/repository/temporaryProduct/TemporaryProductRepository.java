package com.impacus.maketplace.repository.temporaryProduct;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryProductRepository extends JpaRepository<TemporaryProduct, Long> {

    boolean existsByRegisterId(String userId);

    Optional<TemporaryProduct> findByRegisterId(String userId);
}

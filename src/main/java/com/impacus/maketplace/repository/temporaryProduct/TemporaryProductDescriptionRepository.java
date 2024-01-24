package com.impacus.maketplace.repository.temporaryProduct;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryProductDescriptionRepository extends JpaRepository<TemporaryProductDescription, Long> {

    List<TemporaryProductDescription> findByTemporaryProductId(Long temporaryProductId);
}

package com.impacus.maketplace.repository.temporaryProduct;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryProductOptionRepository extends JpaRepository<TemporaryProductOption, Long> {
    List<TemporaryProductOption> findByTemporaryProductId(Long temporaryProductId);
}

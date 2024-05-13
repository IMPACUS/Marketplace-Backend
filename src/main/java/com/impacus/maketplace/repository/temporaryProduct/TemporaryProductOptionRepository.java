package com.impacus.maketplace.repository.temporaryProduct;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductOption;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporaryProductOptionRepository extends JpaRepository<TemporaryProductOption, Long> {
    List<TemporaryProductOption> findByTemporaryProductId(Long temporaryProductId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM TemporaryProductOption t WHERE t.temporaryProductId = :temporaryProductId")
    void deleteByTemporaryProductId(Long temporaryProductId);
}

package com.impacus.maketplace.repository.temporaryProduct;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import com.impacus.maketplace.repository.temporaryProduct.querydsl.TemporaryProductCustomRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryProductRepository extends JpaRepository<TemporaryProduct, Long>, TemporaryProductCustomRepository {

    boolean existsByRegisterId(String userId);

    Optional<TemporaryProduct> findByRegisterId(String userId);

    @Query("SELECT tp.id FROM TemporaryProduct tp where tp.registerId = :registerId")
    Long findIdByRegisterId(@Param("registerId") String userId);
}

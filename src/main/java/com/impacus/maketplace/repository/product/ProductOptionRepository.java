package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.ProductOption;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ProductOption> findByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ProductOption findByProductOptionIdWithWriteLock(Long productOptionId);

    @Transactional
    @Modifying
    @Query("UPDATE ProductOption po SET po.isDeleted = true WHERE po.id in :ids")
    int deleteAllById(@Param("ids") List<Long> ids);

    @Transactional
    @Modifying
    @Query("UPDATE ProductOption po SET po.color = :color, po.size = :size, po.stock = :stock WHERE po.id = :id")
    int updateProductOptionById(
            @Param("id") Long id,
            @Param("color") String color,
            @Param("size") String size,
            @Param("stock") Long stock
    );

    @Transactional
    @Modifying
    @Query("UPDATE ProductOption po SET po.isDeleted = true WHERE po.id in :ids")
    int updateIsDeleteTrueByIds(
            @Param("ids") List<Long> ids
    );

    boolean existsByIsDeletedFalseAndId(Long productOptionId);
}

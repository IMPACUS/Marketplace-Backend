package com.impacus.maketplace.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.repository.product.querydsl.ProductCustomRepository;
import com.impacus.maketplace.repository.product.querydsl.WebProductCustomRepository;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository, WebProductCustomRepository {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    boolean existsByCategoryId(Long subCategoryId);

    Optional<Product> findByIsDeletedFalseAndId(Long productId);

    boolean existsByIsDeletedFalseAndId(Long productId);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.productImages = :productImages WHERE p.id = :id")
    int updateProductImagesById(
            @Param("id") Long id,
            @Param("productImages") List<String> productImages
    );

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.id = :id")
    int updateIsDeleteTrueById(
            @Param("id") Long id
    );
}

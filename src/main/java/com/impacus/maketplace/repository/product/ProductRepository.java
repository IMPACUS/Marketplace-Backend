package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.repository.product.querydsl.ProductCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    boolean existsByCategoryId(Long subCategoryId);

    Optional<Product> findByIsDeletedFalseAndId(Long productId);

    boolean existsByIsDeletedFalseAndId(Long productId);
}

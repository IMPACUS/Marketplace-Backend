package com.impacus.maketplace.repository.product;

import com.impacus.maketplace.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    boolean existsByCategoryId(Long subCategoryId);
}

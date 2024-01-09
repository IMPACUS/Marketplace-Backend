package com.impacus.maketplace.repository;

import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryType(SubCategory category, Pageable pageable);
}

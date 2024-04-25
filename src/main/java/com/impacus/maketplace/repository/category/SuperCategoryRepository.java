package com.impacus.maketplace.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impacus.maketplace.entity.category.SuperCategory;

@Repository
public interface SuperCategoryRepository extends JpaRepository<SuperCategory, Long>, SuperCategoryCustomRepository {
    boolean existsByName(String name);
}

package com.impacus.maketplace.repository.category;

import com.impacus.maketplace.entity.category.SuperCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperCategoryRepository extends JpaRepository<SuperCategory, Long>, SuperCategoryCustomRepository {
    boolean existsByName(String name);
}

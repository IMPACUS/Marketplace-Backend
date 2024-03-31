package com.impacus.maketplace.repository.category;

import com.impacus.maketplace.entity.category.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    boolean existsByName(String name);

    int countBySuperCategoryId(Long superCategoryId);

    List<SubCategory> findBySuperCategoryId(Long superCategoryId);
}

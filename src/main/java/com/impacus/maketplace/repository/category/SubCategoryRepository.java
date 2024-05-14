package com.impacus.maketplace.repository.category;

import com.impacus.maketplace.entity.category.SubCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    boolean existsByName(String name);

    int countBySuperCategoryId(Long superCategoryId);

    List<SubCategory> findBySuperCategoryId(Long superCategoryId);

    @Transactional
    @Modifying
    @Query("UPDATE SubCategory sc SET sc.name = :name WHERE sc.id = :id")
    int updateCategoryName(@Param("id") Long id, @Param("name") String name);
}

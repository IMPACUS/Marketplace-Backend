package com.impacus.maketplace.repository.category;

import com.impacus.maketplace.entity.category.SuperCategory;
import com.impacus.maketplace.repository.category.querydsl.SuperCategoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperCategoryRepository extends JpaRepository<SuperCategory, Long>, SuperCategoryCustomRepository {
    boolean existsByName(String name);

    @Modifying
    @Query("UPDATE SuperCategory sc SET sc.name = :name WHERE sc.id = :id")
    int updateCategoryName(@Param("id") Long id, @Param("name") String name);
}

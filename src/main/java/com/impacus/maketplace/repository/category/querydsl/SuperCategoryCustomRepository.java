package com.impacus.maketplace.repository.category.querydsl;

import com.impacus.maketplace.dto.category.response.CategoryDetailDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SuperCategoryCustomRepository {
    List<CategoryDetailDTO> findAllCategory();

    @Modifying
    @Query("UPDATE SuperCategory sc SET sc.name = :name WHERE sc.id = :id")
    int updateCategoryNameById(@Param("id") Long id, @Param("name") String name);
}

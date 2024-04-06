package com.impacus.maketplace.entity.category;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "sub_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_category_id")
    private Long id;

    @Column(nullable = false)
    private Long superCategoryId;

    @Column(nullable = true)
    private Long thumbnailId;

    @Column(nullable = false, length = 10)
    private String name;

    public SubCategory(Long superCategoryId, Long thumbnailId, String name) {
        this.superCategoryId = superCategoryId;
        this.thumbnailId = thumbnailId;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

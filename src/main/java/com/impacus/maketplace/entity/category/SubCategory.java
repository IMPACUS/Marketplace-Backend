package com.impacus.maketplace.entity.category;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "sub_category")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_category_id")
    private Long id;

    @Column(nullable = false)
    private Long superCategoryId;

    @Column(nullable = false)
    private Long thumbnailId;

    @Column(nullable = false, length = 50)
    private String name;

}

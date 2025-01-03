package com.impacus.maketplace.entity.search;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "popular_search")
@Getter
public class PopularSearch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;
    private Integer count;

    public void update(String keyword, Integer count) {
        this.keyword = keyword;
        this.count = count;
    }
}

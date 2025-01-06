package com.impacus.maketplace.entity.search;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.dto.SearchDTO;
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
    private Long productId;
    private String keyword;
    private Integer count;

    public PopularSearch(SearchDTO searchDTO, int count) {
        this.productId = searchDTO.getSearchId();
        this.keyword = searchDTO.getSearchName();
        this.count = Integer.MAX_VALUE - count;
    }
}

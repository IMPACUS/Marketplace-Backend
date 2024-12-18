package com.impacus.maketplace.dto;

import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.redis.entity.ProductSearch;
import lombok.Getter;

@Getter
public class SearchDTO {
    private String searchName;
    private SearchType type;
    private Long searchId;

    public SearchDTO(ProductSearch p) {
        this.searchName = p.getSearchName();
        this.type = p.getType();
        this.searchId = p.getSearchId();
    }
}

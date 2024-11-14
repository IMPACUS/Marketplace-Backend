package com.impacus.maketplace.redis.entity;

import com.impacus.maketplace.common.enumType.SearchType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "productSearch")
public class ProductSearch {
    @Id
    private String id;

    @Column(nullable = false)
    private String searchName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SearchType type;

    private Long searchId;

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}

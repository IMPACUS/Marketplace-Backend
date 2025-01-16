package com.impacus.maketplace.repository.search;

import com.impacus.maketplace.entity.search.PopularSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PopularSearchRepository extends JpaRepository<PopularSearch, Long> {
    @Modifying
    @Query(value = "truncate popular_search", nativeQuery = true)
    void truncateTable();

}

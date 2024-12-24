package com.impacus.maketplace.repository.search;

import com.impacus.maketplace.entity.search.PopularSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PopularSearchRepository extends JpaRepository<PopularSearch, Long> {
   Optional<PopularSearch> findByKeyword(String keyword);

   List<PopularSearch> findTop10ByOrderByCountDesc();
}

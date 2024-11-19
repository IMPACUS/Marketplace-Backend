package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.redis.entity.RecentSearch;
import com.impacus.maketplace.redis.repository.RecentSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecentSearchService {
    private final RecentSearchRepository recentSearchRepository;

    public void addSearch(String search, Long userId) {
        Optional<RecentSearch> optional = recentSearchRepository.findByUserId(userId.toString());
        if (optional.isEmpty()) {
            recentSearchRepository.save(new RecentSearch(userId.toString(), search));
        } else {
            RecentSearch recentSearch = optional.get();
            recentSearch.add(search);
            recentSearchRepository.save(recentSearch);
        }
    }

    public LinkedList<String> getSearch(Long userId) {
        return recentSearchRepository.findById(userId.toString())
                .map(RecentSearch::getSearchList)
                .orElse(new LinkedList<>());
    }
}

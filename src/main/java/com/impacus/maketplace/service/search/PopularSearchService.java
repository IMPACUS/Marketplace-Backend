package com.impacus.maketplace.service.search;

import com.impacus.maketplace.entity.search.PopularSearch;
import com.impacus.maketplace.redis.service.PopularSearchRedisService;
import com.impacus.maketplace.repository.search.PopularSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class PopularSearchService {
    private final PopularSearchRedisService redisService;
    private final PopularSearchRepository popularSearchRepository;

    // redis와 db 저장
    public void addKeyword(String keyword) {
        redisService.incrementKeyword(keyword, 1);
        updateKeywordInDB(keyword);
    }

    public void updateKeywordInDB(String keyword) {
        Double score = redisService.getKeywordScore(keyword);

        if (score != null) {
            PopularSearch popularSearch = popularSearchRepository.findByKeyword(keyword)
                    .orElse(new PopularSearch());
            popularSearch.update(keyword, score.intValue());
            popularSearchRepository.save(popularSearch);
        }
    }

    public List<Object> getPopularKeywords() {
        return redisService.getTopKeywords().stream().toList();
    }


    // 3일마다 redis 초기화하고 데이터베이스에서 top10 호출하여 저장
    @Scheduled(cron = "0 0 0 1/3 * *", zone = "Asia/Seoul")
    public void schedule() {
        redisService.deleteAll();
        List<PopularSearch> top10 = popularSearchRepository.findTop10ByOrderByCountDesc();
        for (PopularSearch p : top10) {
            redisService.incrementKeyword(p.getKeyword(), p.getCount());
        }
    }
}

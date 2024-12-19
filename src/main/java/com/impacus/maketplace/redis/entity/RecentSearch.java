package com.impacus.maketplace.redis.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.LinkedList;

@RedisHash(value = "recentSearch", timeToLive = 1814400) // 3주
@NoArgsConstructor
@Getter
public class RecentSearch {
    @Id
    private String userId;
    private LinkedList<String> searchList;
    private LocalDateTime updateAt;

    public RecentSearch(String userId, String search) {
        this.userId = userId;
        this.searchList = new LinkedList<>();
        this.searchList.add(search);
    }

    public void add(String search) {
        if (this.searchList.contains(search))
            searchList.remove(search); // 중복 제거 후 맨 앞으로 이동

        this.searchList.addFirst(search);

        // 최대 10개의 검색어만 유지
        if (this.searchList.size() > 10)
            this.searchList.removeLast();

        this.updateAt = LocalDateTime.now();
    }
}

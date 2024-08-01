package com.goormy.hackathon.repository;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedUserSortRedisRepository_DS {

    private final RedisTemplate<String, Long> redisTemplate;
    private static final String FEED_USER_SORTED_KEY = "feedusersort:";

    private ListOperations<String, Long> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    public void add(Long userId, Long postId) {
        listOperations.leftPush(FEED_USER_SORTED_KEY + userId, postId);
    }

    public List<Long> getSome(Long userId, int size) {
        return listOperations.rightPop(FEED_USER_SORTED_KEY + userId, size);
    }

    public List<Long> getAll(Long userId) {
        return listOperations.rightPop(FEED_USER_SORTED_KEY + userId, Long.MAX_VALUE);
    }
}

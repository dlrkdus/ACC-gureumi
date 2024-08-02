package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.PostSimpleInfo_DS;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedUserRedisRepository_DS {

    private final RedisTemplate<String, PostSimpleInfo_DS> redisTemplate;
    private static final String FEED_USER_KEY = "feeduser:";

    private ListOperations<String, PostSimpleInfo_DS> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    public void add(Long userId, PostSimpleInfo_DS value) {
        listOperations.leftPush(FEED_USER_KEY + userId, value);
    }

    public PostSimpleInfo_DS get(Long userId) {
        return listOperations.rightPop(FEED_USER_KEY + userId);
    }

    public List<PostSimpleInfo_DS> getAll(Long userId) {
        return listOperations.rightPop(FEED_USER_KEY + userId, Long.MAX_VALUE);
    }
}

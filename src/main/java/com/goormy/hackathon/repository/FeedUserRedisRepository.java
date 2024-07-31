package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.PostSimpleInfo;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedUserRedisRepository {

    private final RedisTemplate<String, PostSimpleInfo> redisTemplate;
    private static final String FEED_USER_KEY = "feed_user:";

    private ListOperations<String, PostSimpleInfo> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    public void add(Long userId, PostSimpleInfo value) {
        listOperations.leftPush(FEED_USER_KEY + userId, value);
    }

    public PostSimpleInfo get(Long userId) {
        return listOperations.rightPop(FEED_USER_KEY + userId);
    }

    public List<PostSimpleInfo> getAll(Long userId) {
        return listOperations.rightPop(FEED_USER_KEY + userId, Long.MAX_VALUE);
    }
}

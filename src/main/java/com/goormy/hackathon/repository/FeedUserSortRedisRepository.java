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
public class FeedUserSortRedisRepository {
    private final RedisTemplate<String, PostSimpleInfo> redisTemplate;
    private static final String FEED_USER_SORTED_KEY = "feed_user_sort:";

    private ListOperations<String, PostSimpleInfo> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    public void add(Long userId, PostSimpleInfo value) {
        listOperations.leftPush(FEED_USER_SORTED_KEY + userId, value);
    }

    public List<PostSimpleInfo> getSome(Long userId, int size) {
        return listOperations.rightPop(FEED_USER_SORTED_KEY + userId, size);
    }

    public List<PostSimpleInfo> getAll(Long userId) {
        return listOperations.rightPop(FEED_USER_SORTED_KEY + userId, Long.MAX_VALUE);
    }
}

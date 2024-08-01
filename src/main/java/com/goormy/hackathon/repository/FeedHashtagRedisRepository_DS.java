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
public class FeedHashtagRedisRepository_DS {

    private final RedisTemplate<String, PostSimpleInfo_DS> redisTemplate;
    private static final String FEED_HASHTAG_KEY = "feedhashtag:";

    private ListOperations<String, PostSimpleInfo_DS> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    public void add(Long hashtagId, PostSimpleInfo_DS value) {
        listOperations.leftPush(FEED_HASHTAG_KEY + hashtagId, value);
    }

    public List<PostSimpleInfo_DS> getAll(Long hashtagId) {
        return listOperations.range(FEED_HASHTAG_KEY + hashtagId, 0, -1);
    }
}

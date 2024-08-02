package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.redis.entity.FeedSimpleInfo_SY;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedUserRedisRepository_SY {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(Long userId, Post post) {
        String key = "feeduser:" + userId;
        Object value = new FeedSimpleInfo_SY(post);
        redisTemplate.opsForList().leftPush(key, value);
    }

}

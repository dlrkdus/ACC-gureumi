package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.redis.entity.FeedSimpleInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedUserRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(Long userId, Post post) {
        String key = "PostUser:" + userId;
        Object value = new FeedSimpleInfo(post);
        redisTemplate.opsForList().leftPush(key, value);
    }

}

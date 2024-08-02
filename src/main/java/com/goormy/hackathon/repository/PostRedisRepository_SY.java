package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.PostCache_SY;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRedisRepository_SY {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(PostCache_SY postCacheSY) {
        redisTemplate.opsForList().leftPush(postCacheSY.getKey(), postCacheSY);
    }

}

package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.PostCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(PostCache postCache) {
        redisTemplate.opsForList().leftPush(postCache.getKey(), postCache);
    }

}

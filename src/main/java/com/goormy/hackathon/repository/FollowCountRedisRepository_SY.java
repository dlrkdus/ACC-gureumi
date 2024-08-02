package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.FollowCountCache_SY;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowCountRedisRepository_SY {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(FollowCountCache_SY followCountCacheSY) {
        redisTemplate.opsForHash().put(followCountCacheSY.getKey(), followCountCacheSY.getField(), followCountCacheSY.getFollowCount());
    }

    public Integer findFollowCountByHashtagId(Long hashtagId) {
        String key = "followcount:" + hashtagId;
        String field = String.valueOf(hashtagId);
        return (Integer) redisTemplate.opsForHash().get(key, field);
    }

}

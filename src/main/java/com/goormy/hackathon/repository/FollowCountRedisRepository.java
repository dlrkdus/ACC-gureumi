package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.FollowCountCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowCountRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(FollowCountCache followCountCache) {
        redisTemplate.opsForHash().put(followCountCache.getKey(), followCountCache.getField(), followCountCache.getFollowCount());
    }

    public Integer findFollowCountByHashtagId(Long hashtagId) {
        String key = "followcount:" + hashtagId;
        String field = String.valueOf(hashtagId);
        return (Integer) redisTemplate.opsForHash().get(key, field);
    }

}

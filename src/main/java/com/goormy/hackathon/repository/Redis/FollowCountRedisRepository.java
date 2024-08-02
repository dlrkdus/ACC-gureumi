
package com.goormy.hackathon.repository.Redis;

import com.goormy.hackathon.redis.entity.FollowCountCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FollowCountRedisRepository {

    private final RedisTemplate<String, Integer> integerRedisTemplate;
    private static final String FOLLOWING_COUNT_KEY = "follow_count:";
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public FollowCountRedisRepository(RedisTemplate<String, Integer> integerRedisTemplate
    , RedisTemplate<String, Object> redisTemplate) {
        this.integerRedisTemplate = integerRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    public Integer getFollowCount(Long hashtagId) {
        String key = FOLLOWING_COUNT_KEY + hashtagId;
        return integerRedisTemplate.opsForValue().get(key);
    }

    public void setFollowCount(Long hashtagId, int followCount) {
        String key = FOLLOWING_COUNT_KEY + hashtagId;
        integerRedisTemplate.opsForValue().set(key, followCount);
    }

    public void incrementFollowCount(Long hashtagId) {
        String key = FOLLOWING_COUNT_KEY + hashtagId;
        integerRedisTemplate.opsForValue().increment(key, 1);
    }

    public void decrementFollowCount(Long hashtagId) {
        String key = FOLLOWING_COUNT_KEY + hashtagId;
        integerRedisTemplate.opsForValue().decrement(key, 1);
    }

    public void set(FollowCountCache followCountCacheSY) {
        redisTemplate.opsForHash().put("followcount:" + followCountCacheSY.getHashtagId(), followCountCacheSY.getField(), followCountCacheSY.getFollowCount());
    }


    public Integer findFollowCountByHashtagId(Long hashtagId) {
        String key = "followcount:" + hashtagId;
        String field = String.valueOf(hashtagId);
        return (Integer) redisTemplate.opsForHash().get(key, field);
    }
}

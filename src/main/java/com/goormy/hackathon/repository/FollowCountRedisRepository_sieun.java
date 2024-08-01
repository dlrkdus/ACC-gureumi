
package com.goormy.hackathon.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FollowCountRedisRepository_sieun {

    private final RedisTemplate<String, Integer> integerRedisTemplate;
    private static final String FOLLOWING_COUNT_KEY = "follow_count:";

    @Autowired
    public FollowCountRedisRepository_sieun(RedisTemplate<String, Integer> integerRedisTemplate) {
        this.integerRedisTemplate = integerRedisTemplate;
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
}

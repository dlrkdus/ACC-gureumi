package com.goormy.hackathon.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FollowRedisRepository {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public void insertFollow(Long hashtagId, Long userId) {
        String key = "hashtagId: " + hashtagId.toString();
        redisTemplate.opsForList().rightPush(key, userId.toString());
    }

    public void removeFollow(Long hashtagId, Long userId) {
        String key = "hashtagId:" + hashtagId.toString();
        redisTemplate.opsForList().remove(key, 0, userId.toString());
    }

    public void removeAllFollows(Long hashtagId) {
        String key = "hashtagId:" + hashtagId.toString();
        redisTemplate.delete(key);
    }}

package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.redis.entity.FeedSimpleInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedHashtagRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(Long hashtagId, Post post) {
        String key = "feedhashtag:" + hashtagId;
        Object value = new FeedSimpleInfo(post);
        redisTemplate.opsForList().leftPush(key, value);
    }

}

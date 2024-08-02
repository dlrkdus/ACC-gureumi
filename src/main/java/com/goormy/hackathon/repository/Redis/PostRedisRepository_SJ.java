package com.goormy.hackathon.repository.Redis;

import com.goormy.hackathon.redis.entity.PostRedis_DS;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PostRedisRepository_SJ {
    private final RedisTemplate<String, Object> redisTemplate;

    // 포스트 삭제
    public void delete(Long postId) {
        String key = "post:" + postId;
        redisTemplate.delete(key);
    }

    public void set(PostRedis_DS postRedis) {
        redisTemplate.opsForValue().set("post:" + postRedis.getId(), postRedis);
    }

}

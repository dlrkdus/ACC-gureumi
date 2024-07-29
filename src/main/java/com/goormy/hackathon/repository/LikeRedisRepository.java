package com.goormy.hackathon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class LikeRedisRepository {

    private final RedisTemplate<String, Integer> redisTemplate;

    public void update(Long postId, Long userId, Integer value) {
        String key = "postlike:" + postId.toString();
        String field = userId.toString();

        redisTemplate.opsForHash().put(key, field, value);
    }


    public void delete(Long postId, Long userId){
        String key = "postlike:" + postId.toString();
        String field = userId.toString();

        redisTemplate.opsForHash().delete(key, field);
    }

    public Integer findPostLikeFromCache(Long postId, Long userId) {
        String key = "postlike:" + postId.toString();
        String field = userId.toString();

        return (Integer) redisTemplate.opsForHash().get(key, field);
    }

    public Map<Object, Object> findByKey(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Set<String> findKeys() {
        Set<String> keys = redisTemplate.keys("postlike:*");

        System.out.println(keys);
        return keys;
    }

    //    # PostId로 조회하는 함수
//    public Map<Long, Integer> findByPostId(Long postId) {
//        String key = "postlike:" + postId.toString();
//        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
//
//        return entries.entrySet().stream()
//                .collect(Collectors.toMap(
//                        entry -> Long.valueOf((String) entry.getKey()),
//                        entry -> (Integer) entry.getValue()
//                ));
//    }
}

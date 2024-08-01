package com.goormy.hackathon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class LikeRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * @description '좋아요' 정보를 업데이트 하는 함수
     * */
    public void set(Long postId, Long userId, Integer value) {
        String key = "postlike:" + postId.toString();
        String field = userId.toString();

        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * @description '좋아요' 혹은 '좋아요 취소' 정보를 삭제하는 함수
     * */
    public void delete(Long postId, Long userId){
        String key = "postlike:" + postId.toString();
        String field = userId.toString();

        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * @description postId와 userId에 대한 value를 조회
     * */
    public Integer findPostLikeByPostIdAndUserId(Long postId, Long userId) {
        String key = "postlike:" + postId.toString();
        String field = userId.toString();

        return (Integer) redisTemplate.opsForHash().get(key, field);
    }

    /**
     * @description Key에 대한 모든 field와 value를 조회
     * */
    public Map<Object, Object> findPostLikeByKey(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @description Key에 대한 모든 field와 value를 삭제
     * */
    public void delete(String key) {
        redisTemplate.delete(key);
    }




    public Set<String> findAllKeys() {
        Set<String> keys = redisTemplate.keys("postlike:*");

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

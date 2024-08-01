package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class FollowRedisRepository {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    FollowRepository followRepository;

    public void insertFollow(Long hashtagId, Long userId) {
        String key = "hashtagId: " + hashtagId.toString();
        redisTemplate.opsForList().rightPush(key, userId);
    }

    public void removeFollow(Long hashtagId, Long userId) {
        String key = "hashtagId:" + hashtagId.toString();
        redisTemplate.opsForList().remove(key, 0, userId);
    }

    public List<Follow> getAllFollows() {
        // Redis에서 데이터를 가져와 RDBMS로 마이그레이션
        List<Follow> follows = new ArrayList<>();

        // 모든 키 가져오기
        Set<String> keys = redisTemplate.keys("hashtagId:*");

        if (keys != null) {
            for (String key : keys) {
                List<Long> userIds = (List<Long>) (List<?>) redisTemplate.opsForList().range(key, 0, -1);
                if (userIds != null) {
                    for (Long userId : userIds) {
                        Long hashtagId = Long.parseLong(key.split(":")[1]);
                        Follow follow = followRepository.findByUserIdAndHashTagId(userId,hashtagId)
                                .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우입니다. userId: " + userId + " hashtagId: " + hashtagId));
                        follows.add(follow);
                    }
                }
            }
        }

        return follows;
    }




}

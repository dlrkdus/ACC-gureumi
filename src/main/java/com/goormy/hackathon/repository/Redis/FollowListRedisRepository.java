package com.goormy.hackathon.repository.Redis;

import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.repository.JPA.FollowRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class FollowListRedisRepository {

    private final RedisTemplate<String, Long> redisTemplate;
    private final FollowRepository followRepository;

    private static final String FEED_USER_SORTED_KEY = "hashtagId:";

    private ListOperations<String, Long> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    public void set(Long hashtagId, Long userId) {
        listOperations.leftPush(FEED_USER_SORTED_KEY + hashtagId, userId);
    }

    public void delete(Long hashtagId, Long userId) {
        listOperations.trim(FEED_USER_SORTED_KEY + hashtagId, 0, -1);
    }

    public List<Follow> getAllFollows() {
        Set<String> keys = redisTemplate.keys("hashtagId:*");
        List<Follow> follows = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {
                List<Long> userIds = listOperations.range(key, 0, -1);
                if (userIds != null) {
                    for (Long userId : userIds) {
                        Long hashtagId = Long.parseLong(key.split(":")[1]);
                        Follow follow = followRepository.findByUserIdAndHashTagId(userId, hashtagId)
                                .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우입니다. userId: " + userId + " hashtagId: " + hashtagId));
                        follows.add(follow);
                    }
                }
            }
        }
        return follows;
    }

    public List<Long> findUserIdListByHashtagId(Long hashtagId) {
            return listOperations.range(FEED_USER_SORTED_KEY + hashtagId, 0, -1);
    }

}

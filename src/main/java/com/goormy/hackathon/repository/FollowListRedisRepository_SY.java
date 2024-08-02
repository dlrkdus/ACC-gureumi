package com.goormy.hackathon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowListRedisRepository_SY {

    private final RedisTemplate<String, Object> redisTemplate;

    // TODO: 수정 필요 - save 하는 쪽이 어떤 식으로 저장하느냐에 따라 호출 구현이 다를 듯
    public List<Integer> findUserIdListByHashtagId(Long hashtagId) {
        String key = "followlist:" + hashtagId;
        return (List<Integer>) redisTemplate.opsForValue().get(key);
    }

}

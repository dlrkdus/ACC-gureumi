package com.goormy.hackathon.repository.Redis;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.redis.entity.FeedSimpleInfo_SY;
import com.goormy.hackathon.redis.entity.PostSimpleInfo;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedUserRedisRepository {

    private final RedisTemplate<String, PostSimpleInfo> redisTemplate;
    private static final String FEED_USER_KEY = "feeduser:";
    private final RedisTemplate<String, Object> redisTemplate_feedUser;

    private ListOperations<String, PostSimpleInfo> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    public void add(Long userId, PostSimpleInfo value) {
        listOperations.leftPush(FEED_USER_KEY + userId, value);
    }

    public PostSimpleInfo get(Long userId) {
        return listOperations.rightPop(FEED_USER_KEY + userId);
    }

    public List<PostSimpleInfo> getAll(Long userId) {
        return listOperations.rightPop(FEED_USER_KEY + userId, Long.MAX_VALUE);
    }


    public void set(Long userId, Post post) {
        String key = "feeduser:" + userId;
        Object value = new FeedSimpleInfo_SY(post);
        redisTemplate_feedUser.opsForList().leftPush(key, value);
    }
}

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
public class FeedHashtagRedisRepository {

    private final RedisTemplate<String, PostSimpleInfo> redisTemplate_postSimple;
    private static final String FEED_HASHTAG_KEY = "feedhashtag:";

    private final RedisTemplate<String, Object> redisTemplate_postHashtag;

    private ListOperations<String, PostSimpleInfo> listOperations;

    @PostConstruct
    private void init() {
        listOperations = redisTemplate_postSimple.opsForList();
    }

    public void add(Long hashtagId, PostSimpleInfo value) {
        listOperations.leftPush(FEED_HASHTAG_KEY + hashtagId, value);
    }

    public List<PostSimpleInfo> getAll(Long hashtagId) {
        return listOperations.range(FEED_HASHTAG_KEY + hashtagId, 0, -1);
    }

    public void set(Long hashtagId, Post post) {
        String key = "feedhashtag:" + hashtagId;
        Object value = new FeedSimpleInfo_SY(post);
        redisTemplate_postHashtag.opsForList().leftPush(key, value);
    }
}

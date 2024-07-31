package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.PostRedis;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostRedisRepository {

    private final RedisTemplate<String, PostRedis> redisTemplate;
    private static final String POST_KEY = "Post:";

    private ValueOperations<String, PostRedis> valueOperations;

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void set(Long postId, PostRedis value) {
        valueOperations.set(POST_KEY + postId, value);
    }

    public Optional<PostRedis> get(Long postId) {
        PostRedis post = valueOperations.get(POST_KEY + postId);
        return Optional.ofNullable(post);
    }

    public List<PostRedis> getAll(List<Long> postIdList) {
        return postIdList.stream()
            .map(postId -> valueOperations.get(POST_KEY + postId))
            .toList();
    }
}

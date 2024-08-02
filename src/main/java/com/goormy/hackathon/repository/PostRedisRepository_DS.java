package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.PostRedis_DS;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostRedisRepository_DS {

    private final RedisTemplate<String, PostRedis_DS> redisTemplate;
    private static final String POST_KEY = "post:";

    private ValueOperations<String, PostRedis_DS> valueOperations;

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void set(Long postId, PostRedis_DS value) {
        valueOperations.set(POST_KEY + postId, value);
    }

    public Optional<PostRedis_DS> get(Long postId) {
        PostRedis_DS post = valueOperations.get(POST_KEY + postId);
        return Optional.ofNullable(post);
    }

    public List<PostRedis_DS> getAll(List<Long> postIdList) {
        return postIdList.stream()
            .map(postId -> valueOperations.get(POST_KEY + postId))
            .toList();
    }
}

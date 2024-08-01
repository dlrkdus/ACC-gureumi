package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.UserRedis_DS;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRedisRepository_DS {

    private final RedisTemplate<String, UserRedis_DS> redisTemplate;
    private static final String USER_KEY = "user:";

    private ValueOperations<String, UserRedis_DS> valueOperations;

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void set(Long userId, UserRedis_DS value) {
        valueOperations.set(USER_KEY + userId, value);
    }

    public Optional<UserRedis_DS> get(Long userId) {
        UserRedis_DS user = valueOperations.get(USER_KEY + userId);
        return Optional.ofNullable(user);
    }
}

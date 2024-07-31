package com.goormy.hackathon.repository;

import com.goormy.hackathon.redis.entity.UserRedis;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRedisRepository {

    private final RedisTemplate<String, UserRedis> redisTemplate;
    private static final String USER_KEY = "User:";

    private ValueOperations<String, UserRedis> valueOperations;

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void set(Long userId, UserRedis value) {
        valueOperations.set(USER_KEY + userId, value);
    }

    public Optional<UserRedis> get(Long userId) {
        UserRedis user = valueOperations.get(USER_KEY + userId);
        return Optional.ofNullable(user);
    }
}

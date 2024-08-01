package com.goormy.hackathon.repository;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecentUpdateRedisRepository_DS {

    private final StringRedisTemplate stringRedisTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final String RECENT_UPDATE_KEY = "recentupdate:";
    private static final Duration TTL = Duration.ofDays(3);

    private ValueOperations<String, String> valueOperations;

    @PostConstruct
    private void init() {
        valueOperations = stringRedisTemplate.opsForValue();
    }

    public void set(Long userId, LocalDateTime value) {
        String formattedValue = value.format(FORMATTER);
        valueOperations.set(RECENT_UPDATE_KEY + userId, formattedValue, TTL);
    }

    public Optional<LocalDateTime> get(Long userId) {
        String value = valueOperations.get(RECENT_UPDATE_KEY + userId);
        return Optional.ofNullable(value).map(v -> LocalDateTime.parse(v, FORMATTER));
    }
}

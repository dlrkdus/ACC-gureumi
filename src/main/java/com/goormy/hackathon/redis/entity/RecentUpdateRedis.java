package com.goormy.hackathon.redis.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@RedisHash("recent_update")
@Getter
@Builder
public class RecentUpdateRedis {

    @Id
    private Long id;
    private LocalDateTime recentUpdateTime;


    public static RecentUpdateRedis toEntity(Long id, LocalDateTime recentUpdateTime) {
        return RecentUpdateRedis.builder()
            .id(id)
            .recentUpdateTime(recentUpdateTime)
            .build();
    }
}

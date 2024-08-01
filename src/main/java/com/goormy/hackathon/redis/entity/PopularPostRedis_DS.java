package com.goormy.hackathon.redis.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("popular_post")
@AllArgsConstructor
public class PopularPostRedis_DS {

    private List<Long> postIdList; // 최대 256개의 아티클 저장 -> 1KB
}

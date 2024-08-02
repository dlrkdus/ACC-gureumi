package com.goormy.hackathon.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.JPA.FollowRepository;
import com.goormy.hackathon.repository.JPA.HashtagRepository;
import com.goormy.hackathon.repository.JPA.UserRepository;
import com.goormy.hackathon.repository.Redis.FollowListRedisRepository;
import com.goormy.hackathon.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class MigrateFunction implements Consumer<Object> {

    private final FollowRepository followRepository;
    private final FollowListRedisRepository followListRedisRepository;

    @Override
    public void accept(Object o) {
        List<Follow> follows = followListRedisRepository.getAllFollows();
        followRepository.deleteAll();
        followRepository.saveAll(follows);
        log.info("Redis 데이터를 RDBMS로 옮기고 Redis를 초기화했습니다.");
    }

}

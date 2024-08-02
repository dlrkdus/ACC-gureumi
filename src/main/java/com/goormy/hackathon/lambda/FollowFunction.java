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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Configuration
public class FollowFunction{

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final FollowListRedisRepository followListRedisRepository;
    private final FollowService followServiceSieun;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(FollowFunction.class);


    public FollowFunction(FollowRepository followRepository, UserRepository userRepository, HashtagRepository hashtagRepository, FollowListRedisRepository followListRedisRepository
    , ObjectMapper objectMapper, FollowService followServiceSieun) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.hashtagRepository = hashtagRepository;
        this.followListRedisRepository = followListRedisRepository;
        this.objectMapper = objectMapper;
        this.followServiceSieun = followServiceSieun;
    }

    @Bean
    public Consumer<Map<String, Object>> processFollow() {
        return messageBody -> {
            try {

                List<Map<String, Object>> records = (List<Map<String, Object>>) messageBody.get("Records");
                String bodyString = (String) records.get(0).get("body");
                Map<String, Object> body = objectMapper.readValue(bodyString, Map.class);

                // userId와 hashtagId를 Number로 파싱하고 long으로 변환
                long userId = ((Number) body.get("userId")).longValue();
                long hashtagId = ((Number) body.get("hashtagId")).longValue();
                String action = (String) body.get("action");
                logger.info("userId: {}, hashtagId: {}, action: {}", userId, hashtagId, action);

                User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다. userId: " + userId));
                Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(() -> new RuntimeException("존재하지 않는 해시태그입니다. hashtagId: " + hashtagId));

                if ("follow".equals(action)) {
                    Follow follow = new Follow(user,hashtag);
                    // follow_list:{hashtagId} 저장
                    followListRedisRepository.set(hashtagId, userId);
                    // follow_count:{hashtagId} 저장
                    followServiceSieun.followHashtag(hashtagId);
                    System.out.println("팔로우 성공: " + messageBody);
                } else if ("unfollow".equals(action)) {
                    // follow_list:{hashtagId} 삭제
                    Follow follow = followRepository.findByUserIdAndHashTagId(userId, hashtagId)
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우입니다. userId: " + userId + " hashtagId: " + hashtagId));
                    followListRedisRepository.delete(hashtagId, userId);
                    // follow_count:{hashtagId} 삭제
                    followServiceSieun.unfollowHashtag(hashtagId);
                    System.out.println("팔로우 취소 성공: " + messageBody);
                } else {
                    System.out.println("존재하지 않는 action입니다 : " + action);
                }
            } catch (Exception e) {
                System.err.println("메시지 전송 실패: " + messageBody);
                e.printStackTrace();
            }
        };
    }

    // Redis 데이터를 RDBMS에 저장하고 Redis 비우기
    public void migrateData() {
        // Redis에서 모든 팔로우 데이터 가져오기
        List<Follow> follows = followListRedisRepository.getAllFollows();

        // RDBMS에 배치 저장
        followRepository.deleteAll();
        followRepository.saveAll(follows);

        // Redis 비우기
        System.out.println("Redis 데이터를 RDBMS로 옮기고 Redis를 초기화했습니다.");
    }


}

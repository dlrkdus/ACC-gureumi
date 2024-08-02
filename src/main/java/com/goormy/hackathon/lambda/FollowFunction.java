package com.goormy.hackathon.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.*;
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
    private final FollowRedisRepository followRedisRepository;
    private final FollowCountRedisRepository followCountRedisRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(FollowFunction.class);


    public FollowFunction(FollowRepository followRepository, UserRepository userRepository, HashtagRepository hashtagRepository, FollowRedisRepository followRedisRepository
    , ObjectMapper objectMapper, FollowCountRedisRepository followCountRedisRepositorySieun) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.hashtagRepository = hashtagRepository;
        this.followRedisRepository = followRedisRepository;
        this.objectMapper = objectMapper;
        this.followCountRedisRepository = followCountRedisRepositorySieun;
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
                    followRedisRepository.set(hashtagId, userId);
                    // follow_count:{hashtagId} 저장
                    Integer currentCount = followCountRedisRepository.getFollowCount(hashtagId);
                    if (currentCount == null) {
                        followCountRedisRepository.setFollowCount(hashtagId, 1); // 처음 팔로우인 경우 초기화
                    } else {
                        followCountRedisRepository.incrementFollowCount(hashtagId); // 기존 팔로우 수 증가
                    }
                    System.out.println("팔로우 성공: " + messageBody);
                } else if ("unfollow".equals(action)) {
                    // follow_list:{hashtagId} 삭제
                    Follow follow = followRepository.findByUserIdAndHashTagId(userId, hashtagId)
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우입니다. userId: " + userId + " hashtagId: " + hashtagId));
                    followRedisRepository.delete(hashtagId, userId);
                    // follow_count:{hashtagId} 삭제
                    Integer currentCount = followCountRedisRepository.getFollowCount(hashtagId);
                    if (currentCount != null && currentCount > 0) {
                        followCountRedisRepository.decrementFollowCount(hashtagId); // 팔로우 수 감소
                    }
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
        List<Follow> follows = followRedisRepository.getAllFollows();

        // RDBMS에 배치 저장
        followRepository.deleteAll();
        followRepository.saveAll(follows);

        // Redis 비우기
        System.out.println("Redis 데이터를 RDBMS로 옮기고 Redis를 초기화했습니다.");
    }


}

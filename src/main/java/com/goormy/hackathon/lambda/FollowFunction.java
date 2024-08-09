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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class FollowFunction implements Consumer<Object> {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final FollowListRedisRepository followListRedisRepository;
    private final FollowService followService;
    private final ObjectMapper objectMapper;

    @Override
    public void accept(Object messageBody) {
        try {
            String messageString = new String((byte[]) messageBody, StandardCharsets.UTF_8);

            Map<String, Object> messageMap = objectMapper.readValue(messageString, Map.class);
            List<Map<String, Object>> records = (List<Map<String, Object>>) messageMap.get("Records");
            String bodyString = (String) records.get(0).get("body");
            Map<String, Object> body = objectMapper.readValue(bodyString, Map.class);


            // userId와 hashtagId를 Number로 파싱하고 long으로 변환
            long userId = ((Number) body.get("userId")).longValue();
            long hashtagId = ((Number) body.get("hashtagId")).longValue();
            String action = (String) body.get("action");
            log.info("userId: {}, hashtagId: {}, action: {}", userId, hashtagId, action);

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다. userId: " + userId));
            Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(() -> new RuntimeException("존재하지 않는 해시태그입니다. hashtagId: " + hashtagId));

            if ("follow".equals(action)) {
                Follow follow = new Follow(user,hashtag);
                // follow_list:{hashtagId} 저장
                followListRedisRepository.set(hashtagId, userId);
                // follow_count:{hashtagId} 저장
                followService.followHashtag(hashtagId);
                log.info("팔로우 성공: " + messageBody);
                System.out.println("팔로우 성공: " + messageBody);
            } else if ("unfollow".equals(action)) {
                // follow_list:{hashtagId} 삭제
                Follow follow = followRepository.findByUserIdAndHashTagId(userId, hashtagId)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우입니다. userId: " + userId + " hashtagId: " + hashtagId));
                followListRedisRepository.delete(hashtagId, userId);
                // follow_count:{hashtagId} 삭제
                followService.unfollowHashtag(hashtagId);
                log.info("팔로우 취소 성공: " + messageBody);
            } else {
                log.warn("존재하지 않는 action입니다 : " + action);
            }
        } catch (Exception e) {
            log.error("메시지 전송 실패: " + messageBody, e);
        }
    }
}




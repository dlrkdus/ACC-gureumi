package com.goormy.hackathon.lambda;

import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.FollowRepository;
import com.goormy.hackathon.repository.HashtagRepository;
import com.goormy.hackathon.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Consumer;

@Configuration
public class FollowFunction{

    @Bean
    public Consumer<Map<String, Object>> processFollow(FollowRepository followRepository, UserRepository userRepository, HashtagRepository hashtagRepository) {
        return messageBody -> {
            try {
                // userId와 hashtagId를 Number로 파싱하고 long으로 변환
                long userId = ((Number) messageBody.get("userId")).longValue();
                long hashtagId = ((Number) messageBody.get("hashtagId")).longValue();
                String action = (String) messageBody.get("action");

                User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다. userId: " + userId));
                Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(() -> new RuntimeException("존재하지 않는 해시태그입니다. hashtagId: " + hashtagId));

                if ("follow".equals(action)) {
                    Follow follow = new Follow(user,hashtag);
                    followRepository.save(follow);
                    System.out.println("팔로우 성공: " + messageBody);
                } else if ("unfollow".equals(action)) {
                    Follow follow = followRepository.findByUserIdAndHashTagId(userId, hashtagId)
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우입니다. userId: " + userId + " hashtagId: " + hashtagId));
                    followRepository.delete(follow);
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
}

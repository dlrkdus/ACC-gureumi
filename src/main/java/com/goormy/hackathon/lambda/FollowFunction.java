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

                User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다. userId: " + userId));
                Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(() -> new RuntimeException("존재하지 않는 해시태그입니다. hashtagId: " + hashtagId));

                // Follow 객체 생성 및 설정
                Follow follow = new Follow();
                follow.setUser(user);
                follow.setHashtag(hashtag);

                // MySQL에 데이터 저장
                followRepository.save(follow);

                System.out.println("Processed message: " + messageBody);
            } catch (Exception e) {
                System.err.println("Failed to process message: " + messageBody);
                e.printStackTrace();
            }
        };
    }
}

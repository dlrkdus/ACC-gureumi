package com.goormy.hackathon.repository.Redis;

import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.JPA.HashtagRepository;
import com.goormy.hackathon.repository.JPA.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FollowListRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashtagRepository hashtagRepository;

    private final UserRepository userRepository;

    public void set(Long hashtagId, Long userId) {
        String key = "hashtagId:" + hashtagId.toString();
        redisTemplate.opsForList().rightPush(key, userId);
    }

    public void delete(Long hashtagId, Long userId) {
        String key = "hashtagId:" + hashtagId.toString();
        redisTemplate.opsForList().remove(key, 0, userId);
    }

    public List<Follow> getAllFollows() {
        Set<String> keys = redisTemplate.keys("hashtagId:*");
        List<Follow> follows = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {

                List<Object> userIds = redisTemplate.opsForList().range(key, 0, -1);
                if (userIds != null) {
                    for (Object userId : userIds) {
                        Long hashtagId = Long.parseLong(key.split(":")[1]);
                        Long userIdLong = Long.parseLong(String.valueOf(userId));

                        User user = userRepository.findById(userIdLong)
                                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
                        Hashtag hashtag = hashtagRepository.findById(hashtagId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 해시태그 찾을 수 없습니다. "));
                        Follow follow = new Follow(user, hashtag);
                        follows.add(follow);
                    }
                }
            }
        }
        return follows;
    }

    // TODO: 수정 필요 - save 하는 쪽이 어떤 식으로 저장하느냐에 따라 호출 구현이 다를 듯
    public List<Integer> findUserIdListByHashtagId(Long hashtagId) {
        String key = "followlist:" + hashtagId;
        return (List<Integer>) redisTemplate.opsForValue().get(key);
    }




}

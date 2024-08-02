package com.goormy.hackathon.service;

import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.Redis.FollowCountRedisRepository;
import com.goormy.hackathon.repository.JPA.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final FollowCountRedisRepository followCountRedisRepositorySieun;

    // 유저가 팔로우하고 있는 해시태그 목록 조회
    public List<Hashtag> getFollowedHashtags(User user) {
        return followRepository.findHashtagsByUser(user);
    }

    // 팔로우, 언팔로우 캐시 부분
    public void followHashtag(Long hashtagId) {
        Integer currentCount = followCountRedisRepositorySieun.getFollowCount(hashtagId);
        if (currentCount == null) {
            followCountRedisRepositorySieun.setFollowCount(hashtagId, 1); // 처음 팔로우인 경우 초기화
        } else {
            followCountRedisRepositorySieun.incrementFollowCount(hashtagId); // 기존 팔로우 수 증가
        }
    }

    public void unfollowHashtag(Long hashtagId) {
        Integer currentCount = followCountRedisRepositorySieun.getFollowCount(hashtagId);
        if (currentCount != null && currentCount > 0) {
            followCountRedisRepositorySieun.decrementFollowCount(hashtagId); // 팔로우 수 감소
        }
    }
}

package com.goormy.hackathon.service;

import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.redis.entity.PostCache_SY;
import com.goormy.hackathon.repository.Redis.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCacheService {

    private final PostRedisRepository postRedisRepository;
    private final FollowCountRedisRepository followCountRedisRepository;
    private final FollowListRedisRepository followListRedisRepository;
    private final FeedUserRedisRepository feedUserRedisRepository;
    private final FeedHashtagRedisRepository feedHashtagRedisRepository;

    // TODO: 이벤트로 발행한 후 이벤트를 받아 Redis에 비동기로 저장
    public void cache(Post post) {
        postRedisRepository.set(new PostCache_SY(post));
        for (var hashtag : post.getPostHashtags()) {
            if (isPopular(hashtag)) {
                pullModel(hashtag, post);
            } else {
                pushModel(hashtag, post);
            }
        }
    }

    private boolean isPopular(Hashtag hashtag) {
        var followCount = followCountRedisRepository.findFollowCountByHashtagId(hashtag.getId());
        if (followCount != null ) {
            return followCount >= 5000;
        }
        return false;
    }

    private void pushModel(Hashtag hashtag, Post post) {
        var userIdList = followListRedisRepository.findUserIdListByHashtagId(hashtag.getId());
        userIdList.forEach(userId -> {
            feedUserRedisRepository.set(Long.valueOf(userId), post);
        });
    }

    private void pullModel(Hashtag hashtag, Post post) {
        feedHashtagRedisRepository.set(hashtag.getId(), post);
    }
}

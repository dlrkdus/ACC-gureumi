package com.goormy.hackathon.service;

import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.redis.entity.PostCache_SY;
import com.goormy.hackathon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCacheService_SY {

    private final PostRedisRepository_SY postRedisRepositorySY;
    private final FollowCountRedisRepository_SY followCountRedisRepositorySY;
    private final FollowListRedisRepository_SY followListRedisRepositorySY;
    private final FeedUserRedisRepository_SY feedUserRedisRepositorySY;
    private final FeedHashtagRedisRepository_SY feedHashtagRedisRepositorySY;

    // TODO: 이벤트로 발행한 후 이벤트를 받아 Redis에 비동기로 저장
    public void cache(Post post) {
        postRedisRepositorySY.set(new PostCache_SY(post));
        for (var hashtag : post.getPostHashtags()) {
            if (isPopular(hashtag)) {
                pullModel(hashtag, post);
            } else {
                pushModel(hashtag, post);
            }
        }
    }

    private boolean isPopular(Hashtag hashtag) {
        var followCount = followCountRedisRepositorySY.findFollowCountByHashtagId(hashtag.getId());
        if (followCount != null ) {
            return followCount >= 5000;
        }
        return false;
    }

    private void pushModel(Hashtag hashtag, Post post) {
        var userIdList = followListRedisRepositorySY.findUserIdListByHashtagId(hashtag.getId());
        userIdList.forEach(userId -> {
            feedUserRedisRepositorySY.set(Long.valueOf(userId), post);
        });
    }

    private void pullModel(Hashtag hashtag, Post post) {
        feedHashtagRedisRepositorySY.set(hashtag.getId(), post);
    }
}

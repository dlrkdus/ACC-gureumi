package com.goormy.hackathon.service;

import com.goormy.hackathon.dto.hashtag.PostHashtagRequestDto_SY;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.redis.entity.FollowCountCache_SY;
import com.goormy.hackathon.repository.FollowCountRedisRepository_SY;
import com.goormy.hackathon.repository.HashtagRepository_SY;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagService_SY {

    private final HashtagRepository_SY hashtagRepositorySY;
    private final FollowCountRedisRepository_SY followCountRedisRepositorySY;

    @Transactional
    public List<Hashtag> getOrCreateHashtags(List<PostHashtagRequestDto_SY> hashtagRequestDtos) {
        List<Hashtag> hashtags = new ArrayList<>();
        for (var hashtagRequestDto : hashtagRequestDtos) {
            // 이미 존재하는 해시태그인 경우
            var hashtag = hashtagRepositorySY.findByName(hashtagRequestDto.name())
                    .orElseGet(() -> {
                        // 새로운 해시태그인 경우
                        var newHashtag = new Hashtag(hashtagRequestDto.name(), hashtagRequestDto.type());
                        hashtagRepositorySY.save(newHashtag);

                        var followCountCache = new FollowCountCache_SY(newHashtag);
                        followCountRedisRepositorySY.set(followCountCache);

                        return newHashtag;
                    });
            hashtags.add(hashtag);
        }
        return hashtags;
    }

}

package com.goormy.hackathon.service;

import com.goormy.hackathon.dto.hashtag.PostHashtagRequestDto;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.redis.entity.FollowCountCache;
import com.goormy.hackathon.repository.FollowCountRedisRepository;
import com.goormy.hackathon.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final FollowCountRedisRepository followCountRedisRepository;

    @Transactional
    public List<Hashtag> getOrCreateHashtags(List<PostHashtagRequestDto> hashtagRequestDtos) {
        List<Hashtag> hashtags = new ArrayList<>();
        for (var hashtagRequestDto : hashtagRequestDtos) {
            // 이미 존재하는 해시태그인 경우
            var hashtag = hashtagRepository.findByName(hashtagRequestDto.name())
                    .orElseGet(() -> {
                        // 새로운 해시태그인 경우
                        var newHashtag = new Hashtag(hashtagRequestDto.name(), hashtagRequestDto.type());
                        hashtagRepository.save(newHashtag);

                        var followCountCache = new FollowCountCache(newHashtag);
                        followCountRedisRepository.set(followCountCache);

                        return newHashtag;
                    });
            hashtags.add(hashtag);
        }
        return hashtags;
    }

}

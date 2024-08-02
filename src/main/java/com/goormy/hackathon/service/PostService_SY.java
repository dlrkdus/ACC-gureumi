package com.goormy.hackathon.service;

import com.goormy.hackathon.dto.post.PostRequestDto_SY;
import com.goormy.hackathon.dto.post.PostResponseDto_SY;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.repository.PostRepository_SY;
import com.goormy.hackathon.repository.UserRepository_SY;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService_SY {

    private final UserRepository_SY userRepositorySY;
    private final PostRepository_SY postRepositorySY;
    private final HashtagService_SY hashtagServiceSY;
    private final PostCacheService_SY postCacheServiceSY;

    @Transactional
    public PostResponseDto_SY createPost(Long userId, PostRequestDto_SY postRequestDtoSY) {
        // 사용자 찾기
        var user = userRepositorySY.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found"));  // TODO: Custom exception

        // post 생성
        var post = Post.builder()
                .user(user)
                .content(postRequestDtoSY.content())
                .imageUrl(postRequestDtoSY.imageUrl())
                .star(postRequestDtoSY.star())
                .likeCount(0)
                .build();

        // hashtag 생성
        var postHashtags = hashtagServiceSY.getOrCreateHashtags(postRequestDtoSY.postHashtags());
        post.setPostHashtags(postHashtags);

        // DB 저장
        postRepositorySY.save(post);

        // redis 저장
        postCacheServiceSY.cache(post);

        return new PostResponseDto_SY(post);
    }

}

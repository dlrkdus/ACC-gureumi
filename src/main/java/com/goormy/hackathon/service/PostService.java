package com.goormy.hackathon.service;

import com.goormy.hackathon.dto.post.PostRequestDto;
import com.goormy.hackathon.dto.post.PostResponseDto;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.redis.entity.PostCache;
import com.goormy.hackathon.repository.PostRepository;
import com.goormy.hackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HashtagService hashtagService;
    private final PostCacheService postCacheService;

    @Transactional
    public PostResponseDto createPost(Long userId, PostRequestDto postRequestDto) {
        // 사용자 찾기
        var user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found"));  // TODO: Custom exception

        // post 생성
        var post = Post.builder()
                .user(user)
                .content(postRequestDto.content())
                .imageUrl(postRequestDto.imageUrl())
                .star(postRequestDto.star())
                .likeCount(0)
                .build();

        // hashtag 생성
        var postHashtags = hashtagService.getOrCreateHashtags(postRequestDto.postHashtags());
        post.setPostHashtags(postHashtags);

        // DB 저장
        postRepository.save(post);

        // redis 저장
        postCacheService.cache(post);

        return new PostResponseDto(post);
    }

}

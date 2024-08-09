package com.goormy.hackathon.service;

import com.goormy.hackathon.dto.post.PostRequestDto_SY;
import com.goormy.hackathon.dto.post.PostResponseDto_SY;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.repository.JPA.PostRepository;
import com.goormy.hackathon.repository.JPA.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public PostResponseDto_SY createPost(Long userId, PostRequestDto_SY postRequestDtoSY) {
        // 사용자 찾기
        var user = userRepository.findById(userId).orElseThrow(
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
        var postHashtags = hashtagService.getOrCreateHashtags(postRequestDtoSY.postHashtags());
        post.setPostHashtags(postHashtags);

        // DB 저장
        postRepository.save(post);

        // redis 저장
        postCacheService.cache(post);

        return new PostResponseDto_SY(post);
    }

    public Page<Post> getPostsByHashtag(String hashtagName, int page, int size) {
        System.out.println("Fetching posts with hashtag: " + hashtagName + ", page: " + page + ", size: " + size);
        Page<Post> postsPage = postRepository.findPostsByHashtagName(hashtagName, PageRequest.of(page, size));

        if (postsPage == null) {
            throw new RuntimeException("No posts found for the given hashtag.");
        }

        System.out.println("Total elements: " + postsPage.getTotalElements());
        System.out.println("Total pages: " + postsPage.getTotalPages());
        System.out.println("Current page: " + postsPage.getNumber());
        System.out.println("Number of posts on this page: " + postsPage.getNumberOfElements());
        return postsPage;
    }

}

package com.goormy.hackathon.service;

import com.goormy.hackathon.dto.post.PostRequestDto_SY;
import com.goormy.hackathon.dto.post.PostResponseDto_SY;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.repository.PostRepository_SY;
import com.goormy.hackathon.repository.UserRepository_SY;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository_SY userRepositorySY;
    private final PostRepository_SY postRepositorySY;
    private final HashtagService hashtagServiceSY;
    private final PostCacheService postCacheServiceSY;

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

    public Page<Post> getPostsByHashtag(String hashtagName, int page, int size) {
        System.out.println("Fetching posts with hashtag: " + hashtagName + ", page: " + page + ", size: " + size);
        Page<Post> postsPage = postRepositorySY.findPostsByHashtagName(hashtagName, PageRequest.of(page, size));

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

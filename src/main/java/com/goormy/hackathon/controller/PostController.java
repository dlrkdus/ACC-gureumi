package com.goormy.hackathon.controller;


import com.goormy.hackathon.dto.post.PostRequestDto_SY;
import com.goormy.hackathon.dto.post.PostResponseDto_SY;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponseDto_SY create(
            @RequestHeader("userId") Long userId,
            @RequestBody PostRequestDto_SY postRequestDtoSY) {
        return postService.createPost(userId, postRequestDtoSY);
    }

    @GetMapping("/search")
    public Page<Post> getPostsByHashtag(
            @RequestParam(name = "hashtag") String hashtag,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        System.out.println("Received request to fetch posts by hashtag: " + hashtag);
        Page<Post> postsPage = postService.getPostsByHashtag(hashtag, page, size);
        System.out.println("Returning page " + postsPage.getNumber() + " of " + postsPage.getTotalPages() + " for hashtag: " + hashtag);
        return postsPage;
    }

}

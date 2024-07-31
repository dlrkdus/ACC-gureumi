package com.goormy.hackathon.controller;


import com.goormy.hackathon.dto.post.PostRequestDto;
import com.goormy.hackathon.dto.post.PostResponseDto;
import com.goormy.hackathon.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostResponseDto create(
            @RequestHeader("userId") Long userId,
            @RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(userId, postRequestDto);
    }

}

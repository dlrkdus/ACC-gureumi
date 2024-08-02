package com.goormy.hackathon.controller;


import com.goormy.hackathon.dto.post.PostRequestDto_SY;
import com.goormy.hackathon.dto.post.PostResponseDto_SY;
import com.goormy.hackathon.service.PostService_SY;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController_SY {

    private final PostService_SY postServiceSY;

    @PostMapping
    public PostResponseDto_SY create(
            @RequestHeader("userId") Long userId,
            @RequestBody PostRequestDto_SY postRequestDtoSY) {
        return postServiceSY.createPost(userId, postRequestDtoSY);
    }

}

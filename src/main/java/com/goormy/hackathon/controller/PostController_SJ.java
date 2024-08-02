package com.goormy.hackathon.controller;

import com.goormy.hackathon.dto.post.PostRequestDto_SY;
import com.goormy.hackathon.dto.post.PostResponseDto_SJ;
import com.goormy.hackathon.service.PostService_SJ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController_SJ {

    private final PostService_SJ postServiceSJ;

    // 삭제
    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(
            @PathVariable("post_id") Long postId) {
        postServiceSJ.deletePost(postId);
        return ResponseEntity.ok("포스트가 삭제되었습니다. ");
    }

    // 단일 조회
    @GetMapping("/{post_id}")
    public ResponseEntity<PostResponseDto_SJ> getPostByPostId(
            @PathVariable("post_id") Long postId) {
        return ResponseEntity.ok(postServiceSJ.getPost(postId));
    }
}

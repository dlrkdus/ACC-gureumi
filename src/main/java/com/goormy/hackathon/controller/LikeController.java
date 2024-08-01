package com.goormy.hackathon.controller;

import com.goormy.hackathon.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes")
    public ResponseEntity<String> addLike(
        @RequestHeader(name = "userId") Long userId,
        @RequestParam(name = "postId") Long postId) {

        likeService.sendLikeRequest(userId,postId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/likes")
    public ResponseEntity<String> cancelLike(
        @RequestHeader(name = "userId") Long userId,
        @RequestParam(name = "postId") Long postId) {

        likeService.sendCancelLikeRequest(userId,postId);

        return ResponseEntity.noContent().build();
    }

}

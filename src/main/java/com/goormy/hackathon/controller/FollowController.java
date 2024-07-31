package com.goormy.hackathon.controller;

import com.goormy.hackathon.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goormy")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<String> follow(@RequestHeader long userId, @RequestParam long hashtagId) {
        followService.sendFollowRequest(userId,hashtagId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestHeader long userId, @RequestParam long hashtagId) {
        followService.sendUnfollowRequest(userId,hashtagId);
        return ResponseEntity.noContent().build();
    }


}

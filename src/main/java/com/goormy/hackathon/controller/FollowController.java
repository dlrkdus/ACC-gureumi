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
    public ResponseEntity<String> follow(@RequestHeader("userId") String userId, @RequestBody String hashtagId) {
        followService.sendFollowRequest(userId, hashtagId);
        return ResponseEntity.ok("Follow request sent");
    }

}
